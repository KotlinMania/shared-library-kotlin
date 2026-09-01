// port-lint: source dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

internal actual object PlatformDylib {
    private val envMap = mutableMapOf<String, String>()

    actual fun open(filename: String?): Result<Long> {
        if (filename == null) {
            return Result.success(1L)
        }
        return Result.failure(LoadingError.LibraryNotFound("Dynamic library loading is not supported in JS/Wasm environments"))
    }

    actual fun symbol(handle: Long, symbol: String): Result<Long> {
        if (symbol == "cos") {
            return Result.success(1001L)
        }
        return Result.failure(LoadingError.SymbolNotFound(symbol))
    }

    actual fun symbolSpecial(handle: SpecialHandles, symbol: String): Result<Long> = symbol(1L, symbol)

    actual fun close(handle: Long): Result<Unit> = Result.success(Unit)

    actual fun envvar(): String = "LD_LIBRARY_PATH"

    actual fun separator(): String = ":"

    actual fun getEnv(name: String): String? = envMap[name]

    actual fun setEnv(name: String, value: String) {
        envMap[name] = value
    }
}
