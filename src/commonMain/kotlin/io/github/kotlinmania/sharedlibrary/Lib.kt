// port-lint: source lib.rs
package io.github.kotlinmania.sharedlibrary

/**
 * Module ledger and loader helper for shared libraries ported from upstream lib.rs.
 */
public object Lib {
    public const val VERSION: String = "0.1.9"

    /**
     * Tries to open the dynamic library at [path].
     *
     * @param path The filesystem path to the library.
     * @return Result containing [DynamicLibrary] on success, or [LoadingError] on failure.
     */
    public fun open(path: String): Result<DynamicLibrary> =
        DynamicLibrary.open(path)

    /**
     * Try loading the dynamic library at [path].
     *
     * @param path The filesystem path to the library.
     * @return Result containing [DynamicLibrary] on success, or [LoadingError] on failure.
     */
    public fun tryLoading(path: String): Result<DynamicLibrary> =
        DynamicLibrary.open(path)
}
