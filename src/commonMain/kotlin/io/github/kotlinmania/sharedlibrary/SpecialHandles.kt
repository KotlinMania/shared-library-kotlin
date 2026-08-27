// port-lint: source dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

/**
 * Special handles to be used with the `symbolSpecial` function.
 * These are provided by GNU extensions on POSIX/Linux systems.
 */
public enum class SpecialHandles {
    Next,
    Default,
}
