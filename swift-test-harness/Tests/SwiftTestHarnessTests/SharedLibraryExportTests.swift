import Testing
import SharedLibrary

@Suite("SharedLibrary Swift Export Smoke Tests")
struct SharedLibraryExportTests {
    @Test("SharedLibrary swift module imported cleanly")
    func swiftModuleLoads() throws {
        let env = DynamicLibrary.Companion.shared.envvar()
        #expect(!env.isEmpty)

        let sep = DynamicLibrary.Companion.shared.separator()
        #expect(!sep.isEmpty)

        let path = DynamicLibrary.Companion.shared.createPath(paths: ["/usr/lib", "/usr/local/lib"])
        #expect(path == "/usr/lib" + sep + "/usr/local/lib")

        let nextHandle = SpecialHandles.Next
        #expect(nextHandle.description == "Next")
    }
}
