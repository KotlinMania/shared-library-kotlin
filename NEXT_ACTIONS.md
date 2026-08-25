# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 2/2 (100.0%)
- **Function parity:** 15/15 matched (target 25) — 100.0%
- **Class/type parity:** 3/3 matched (target 9) — 100.0%
- **Combined symbol parity:** 18/18 matched (target 34) — 100.0%
- **Average inline-code cosine:** 0.16 (function body across 2 matched files)
- **Average documentation cosine:** 0.62 (doc text across 2 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 2 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. dynamic_library

- **Target:** `sharedlibrary.DynamicLibrary`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 1706.9
- **Functions:** 15/15 matched (target 21)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 2. lib

- **Target:** `sharedlibrary.Lib [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched (target 4)
- **Missing functions:** _none_
- **Types:** 1/1 matched (target 6)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

