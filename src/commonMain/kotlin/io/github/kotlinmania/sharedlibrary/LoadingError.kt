// port-lint: source lib.rs
package io.github.kotlinmania.sharedlibrary

/**
 * Error that can happen while loading the shared library.
 */
public sealed class LoadingError(
    message: String,
) : Exception(message) {
    /**
     * The dynamic library could not be found or loaded.
     *
     * @property descr Description of the failure.
     */
    public data class LibraryNotFound(
        public val descr: String,
    ) : LoadingError("Library not found: $descr")

    /**
     * One of the symbols could not be found in the library.
     *
     * @property symbol The symbol name that was not found.
     */
    public data class SymbolNotFound(
        public val symbol: String,
    ) : LoadingError("Symbol not found: $symbol")
}

/**
 * Common interface for classes wrapping symbols loaded from a [DynamicLibrary].
 */
public interface SharedLibrary {
    public val libraryGuard: DynamicLibrary
}
