// port-lint: source shared_library/src/dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

import kotlinx.cinterop.*
import platform.posix.*
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
internal actual object PlatformDylib {
    actual fun open(filename: String?): Result<Long> {
        val handle = if (filename != null) {
            dlopen(filename, RTLD_LAZY)
        } else {
            dlopen(null, RTLD_LAZY)
        }
        if (handle == null) {
            val err = dlerror()?.toKString() ?: "Unknown error opening library $filename"
            return Result.failure(LoadingError.LibraryNotFound(err))
        }
        return Result.success(handle.rawValue.toLong())
    }

    actual fun symbol(handle: Long, symbol: String): Result<Long> {
        val ptr = interpretCPointer<COpaque>(handle.toNativePtr())
        val sym = dlsym(ptr, symbol)
        if (sym == null) {
            val err = dlerror()?.toKString() ?: "Symbol $symbol not found"
            return Result.failure(LoadingError.SymbolNotFound(err))
        }
        return Result.success(sym.rawValue.toLong())
    }

    actual fun symbolSpecial(handle: SpecialHandles, symbol: String): Result<Long> {
        val targetHandle: CPointer<out CPointed>? = when (handle) {
            SpecialHandles.Next -> interpretCPointer<COpaque>((-1L).toNativePtr())
            SpecialHandles.Default -> null
        }
        val sym = dlsym(targetHandle, symbol)
        if (sym == null) {
            val err = dlerror()?.toKString() ?: "Special symbol $symbol not found"
            return Result.failure(LoadingError.SymbolNotFound(err))
        }
        return Result.success(sym.rawValue.toLong())
    }

    actual fun close(handle: Long): Result<Unit> {
        val ptr = interpretCPointer<COpaque>(handle.toNativePtr())
        val res = dlclose(ptr)
        if (res != 0) {
            val err = dlerror()?.toKString() ?: "Error closing library"
            return Result.failure(Exception(err))
        }
        return Result.success(Unit)
    }

    actual fun envvar(): String = when (Platform.osFamily) {
        OsFamily.MACOSX, OsFamily.IOS, OsFamily.TVOS, OsFamily.WATCHOS -> "DYLD_LIBRARY_PATH"
        else -> "LD_LIBRARY_PATH"
    }

    actual fun separator(): String = ":"

    actual fun getEnv(name: String): String? = getenv(name)?.toKString()

    actual fun setEnv(name: String, value: String) {
        setenv(name, value, 1)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Long.toNativePtr(): NativePtr = NativePtr.NULL + this
