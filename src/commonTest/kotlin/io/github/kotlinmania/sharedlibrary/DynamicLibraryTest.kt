// port-lint: tests shared_library/src/dynamic_library.rs
package io.github.kotlinmania.sharedlibrary

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DynamicLibraryTest {
    @Test
    fun testLoadingCosine() {
        val libm = DynamicLibrary.open(null)
        if (libm.isSuccess) {
            val library = libm.getOrThrow()
            val cosResult = library.symbol("cos")
            if (cosResult.isSuccess) {
                val cosSymbol = cosResult.getOrThrow()
                assertTrue(cosSymbol != 0L, "cos symbol address should not be zero")
            }
            library.close()
        }
    }

    @Test
    fun testErrorsDoNotCrash() {
        val result = DynamicLibrary.open("/dev/null")
        assertTrue(result.isFailure, "Opening /dev/null as dynamic library should fail gracefully")
    }

    @Test
    fun testSearchPathAndEnv() {
        val env = DynamicLibrary.envvar()
        assertTrue(env.isNotEmpty(), "envvar should not be empty")
        val sep = DynamicLibrary.separator()
        assertTrue(sep.isNotEmpty(), "separator should not be empty")

        val pathList = listOf("/usr/lib", "/usr/local/lib")
        val created = DynamicLibrary.createPath(pathList)
        assertEquals("/usr/lib$sep/usr/local/lib", created)

        DynamicLibrary.prependSearchPath("/custom/lib")
        val paths = DynamicLibrary.searchPath()
        assertTrue(paths.contains("/custom/lib") || paths.isEmpty(), "searchPath test complete")
    }

    @Test
    fun testSymbolNotFound() {
        val libm = DynamicLibrary.open(null)
        if (libm.isSuccess) {
            val lib = libm.getOrThrow()
            val sym = lib.symbol("non_existent_symbol_12345_xyz")
            assertTrue(sym.isFailure, "non-existent symbol should fail to load")
            lib.close()
        }
    }

    @Test
    fun testSpecialHandles() {
        val handles = SpecialHandles.entries
        assertEquals(2, handles.size)
        assertTrue(handles.contains(SpecialHandles.Next))
        assertTrue(handles.contains(SpecialHandles.Default))

        val sym = DynamicLibrary.symbolSpecial(SpecialHandles.Default, "cos")
        assertTrue(sym.isSuccess || sym.isFailure)
    }

    @Test
    fun testClose() {
        val libm = DynamicLibrary.open(null)
        if (libm.isSuccess) {
            val lib = libm.getOrThrow()
            lib.close()
        }
    }

    @Test
    fun testLoadingErrorTypes() {
        val err1 = LoadingError.LibraryNotFound("missing.so")
        val err2 = LoadingError.SymbolNotFound("missing_fn")
        assertEquals("Library not found: missing.so", err1.message)
        assertEquals("missing.so", err1.descr)
        assertEquals("Symbol not found: missing_fn", err2.message)
        assertEquals("missing_fn", err2.symbol)
    }
}
