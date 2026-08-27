// port-lint: tests lib.rs
package io.github.kotlinmania.sharedlibrary

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LibTest {
    @Test
    fun testLibVersion() {
        assertEquals("0.1.9", Lib.VERSION)
    }

    @Test
    fun testLibOpenFailsGracefullyOnInvalidPath() {
        val result = Lib.open("/invalid/path/to/missing_lib.so")
        assertTrue(result.isFailure)
    }
}
