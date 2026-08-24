// port-lint: source dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

import kotlinx.cinterop.*
import platform.windows.*

@OptIn(ExperimentalForeignApi::class)
internal actual object PlatformDylib {
    actual fun open(filename: String?): Result<Long> {
        val handle = if (filename != null) {
            LoadLibraryW(filename)
        } else {
            GetModuleHandleW(null)
        }
        if (handle == null) {
            val errCode = GetLastError()
            return Result.failure(LoadingError.LibraryNotFound("Windows error code $errCode opening $filename"))
        }
        return Result.success(handle.rawValue.toLong())
    }

    actual fun symbol(handle: Long, symbol: String): Result<Long> {
        val ptr = interpretCPointer<COpaque>(handle.toNativePtr())
        val sym = GetProcAddress(ptr?.reinterpret(), symbol)
        if (sym == null) {
            val errCode = GetLastError()
            return Result.failure(LoadingError.SymbolNotFound("Symbol $symbol not found (error $errCode)"))
        }
        return Result.success(sym.rawValue.toLong())
    }

    actual fun symbolSpecial(handle: SpecialHandles, symbol: String): Result<Long> {
        return Result.failure(LoadingError.SymbolNotFound(symbol))
    }

    actual fun close(handle: Long): Result<Unit> {
        val ptr = interpretCPointer<COpaque>(handle.toNativePtr())
        val res = FreeLibrary(ptr?.reinterpret())
        if (res == 0) {
            val errCode = GetLastError()
            return Result.failure(Exception("FreeLibrary failed with error $errCode"))
        }
        return Result.success(Unit)
    }

    actual fun envvar(): String = "PATH"

    actual fun separator(): String = ";"

    actual fun getEnv(name: String): String? = platform.posix.getenv(name)?.toKString()

    actual fun setEnv(name: String, value: String) {
        SetEnvironmentVariableW(name, value)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Long.toNativePtr(): NativePtr = NativePtr.NULL + this
