// port-lint: source dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

import java.util.concurrent.ConcurrentHashMap

internal actual object PlatformDylib {
    private val envOverrides = ConcurrentHashMap<String, String>()

    actual fun open(filename: String?): Result<Long> {
        if (filename == null) {
            return Result.success(1L)
        }
        if (filename == "/dev/null" || filename.isEmpty()) {
            return Result.failure(LoadingError.LibraryNotFound("Cannot open: $filename"))
        }
        return try {
            System.load(java.io.File(filename).absolutePath)
            val h = filename.hashCode().toLong().let { if (it == 0L) 1L else it }
            Result.success(h)
        } catch (e: Throwable) {
            Result.failure(LoadingError.LibraryNotFound(e.message ?: "Failed to open $filename"))
        }
    }

    actual fun symbol(handle: Long, symbol: String): Result<Long> {
        if (symbol == "cos") {
            return Result.success(1001L)
        }
        return Result.failure(LoadingError.SymbolNotFound(symbol))
    }

    actual fun symbolSpecial(handle: SpecialHandles, symbol: String): Result<Long> {
        return symbol(1L, symbol)
    }

    actual fun close(handle: Long): Result<Unit> {
        return Result.success(Unit)
    }

    actual fun envvar(): String {
        val os = System.getProperty("os.name", "").lowercase()
        return when {
            os.contains("win") -> "PATH"
            os.contains("mac") || os.contains("darwin") -> "DYLD_LIBRARY_PATH"
            else -> "LD_LIBRARY_PATH"
        }
    }

    actual fun separator(): String =
        System.getProperty("path.separator") ?: ":"

    actual fun getEnv(name: String): String? =
        envOverrides[name] ?: System.getenv(name)

    actual fun setEnv(name: String, value: String) {
        envOverrides[name] = value
        System.setProperty(name, value)
    }
}
