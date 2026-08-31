// port-lint: source shared_library/src/dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

/**
 * Dynamic library facilities.
 *
 * A simple wrapper over the platform's dynamic library facilities.
 */
public class DynamicLibrary internal constructor(
    public val handle: Long,
    public val filename: String? = null,
) : AutoCloseable {
    private var closed: Boolean = false

    /**
     * Drops this dynamic library, releasing resources.
     */
    public fun drop() {
        close()
    }

    /**
     * Returns the address of where symbol [symbol] was loaded into memory.
     *
     * In POSIX compliant systems, returns failure if the symbol was not found
     * in this library or any libraries automatically loaded.
     */
    public fun symbol(symbol: String): Result<Long> {
        if (closed) {
            return Result.failure(LoadingError.SymbolNotFound(symbol))
        }
        return PlatformDylib.symbol(handle, symbol)
    }

    /**
     * Returns the address of symbol [symbol] or null if not found.
     */
    public fun symbolAddress(symbol: String): Long? = symbol(symbol).getOrNull()

    /**
     * Closes this dynamic library handle.
     */
    override fun close() {
        if (!closed) {
            closed = true
            PlatformDylib.close(handle)
        }
    }

    public companion object {
        /**
         * Lazily loads the dynamic library named [filename] into memory and
         * then returns an opaque handle for that dynamic library.
         *
         * Returns a handle to the calling process when passed `null`.
         */
        public fun open(filename: String? = null): Result<DynamicLibrary> =
            if (filename == null) openInternal() else openExternal(filename)

        /**
         * Internal helper to open an external dynamic library.
         */
        public fun openExternal(filename: String): Result<DynamicLibrary> =
            PlatformDylib.open(filename).map { handle ->
                DynamicLibrary(handle = handle, filename = filename)
            }

        /**
         * Internal helper to open the process handle.
         */
        public fun openInternal(): Result<DynamicLibrary> =
            PlatformDylib.open(null).map { handle ->
                DynamicLibrary(handle = handle, filename = null)
            }

        /**
         * Helper checking for error conditions during library operations.
         */
        public fun checkForErrorsIn(action: () -> Long?): Result<Long> {
            val result = action()
            return if (result != null && result != 0L) {
                Result.success(result)
            } else {
                Result.failure(LoadingError.LibraryNotFound("Operation failed"))
            }
        }

        /**
         * Prepends a path to this process's search path for dynamic libraries.
         */
        public fun prependSearchPath(path: String) {
            val current = searchPath().toMutableList()
            current.add(0, path)
            PlatformDylib.setEnv(envvar(), createPath(current))
        }

        /**
         * From a slice of paths, create a new string which is suitable to be an
         * environment variable for this platform's dylib search path.
         */
        public fun createPath(paths: List<String>): String =
            paths.joinToString(separator = PlatformDylib.separator())

        /**
         * Returns the environment variable for this process's dynamic library search path.
         */
        public fun envvar(): String = PlatformDylib.envvar()

        /**
         * Returns the path separator for environment variables on this platform.
         */
        public fun separator(): String = PlatformDylib.separator()

        /**
         * Returns the current search path for dynamic libraries being used by this process.
         */
        public fun searchPath(): List<String> {
            val envValue = PlatformDylib.getEnv(envvar()) ?: return emptyList()
            if (envValue.isEmpty()) return emptyList()
            return envValue.split(PlatformDylib.separator()).filter { it.isNotEmpty() }
        }

        /**
         * Special handles to be used with the symbolSpecial function.
         */
        public fun symbolSpecial(handle: SpecialHandles, symbol: String): Result<Long> =
            PlatformDylib.symbolSpecial(handle, symbol)
    }
}
