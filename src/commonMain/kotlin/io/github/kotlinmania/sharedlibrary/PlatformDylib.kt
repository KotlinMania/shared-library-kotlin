// port-lint: source shared_library/src/dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

internal expect object PlatformDylib {
    fun open(filename: String?): Result<Long>
    fun symbol(handle: Long, symbol: String): Result<Long>
    fun symbolSpecial(handle: SpecialHandles, symbol: String): Result<Long>
    fun close(handle: Long): Result<Unit>
    fun envvar(): String
    fun separator(): String
    fun getEnv(name: String): String?
    fun setEnv(name: String, value: String)
}
