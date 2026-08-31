# port-lint Proposed Changes

**Generated:** 2026-08-31
**Source:** tmp/shared_library/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/sharedlibrary

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/sharedlibrary/PlatformDylib.kt` | `// port-lint: source shared_library/src/dynamic_library.rs` | `// port-lint: source dynamic_library.rs` | `dynamic_library.rs` | `port-lint provenance header matched only after fallback normalization: 'shared_library/src/dynamic_library.rs' vs expected 'dynamic_library.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/sharedlibrary/DynamicLibrary.kt` | `// port-lint: source shared_library/src/dynamic_library.rs` | `// port-lint: source dynamic_library.rs` | `dynamic_library.rs` | `port-lint provenance header matched only after fallback normalization: 'shared_library/src/dynamic_library.rs' vs expected 'dynamic_library.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/sharedlibrary/SpecialHandles.kt` | `// port-lint: source shared_library/src/dynamic_library.rs` | `// port-lint: source dynamic_library.rs` | `dynamic_library.rs` | `port-lint provenance header matched only after fallback normalization: 'shared_library/src/dynamic_library.rs' vs expected 'dynamic_library.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/sharedlibrary/DynamicLibraryTest.kt` | `// port-lint: tests shared_library/src/dynamic_library.rs` | `// port-lint: tests dynamic_library.rs` | `dynamic_library.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:shared_library/src/dynamic_library.rs' vs expected 'dynamic_library.rs'` |
