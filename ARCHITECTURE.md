# FARX Architecture Roadmap

```text
FARX Studio (Android / future desktop)
        |
        v
Editor -> Lexer -> Parser -> AST -> Semantic Analysis
                                  |
                                  v
                         Type System / Scope
                                  |
                                  v
                           FARX IR / Optimizer
                                  |
                    +-------------+-------------+
                    v                           v
               Runtime VM                 Code Generator
                    |                           |
                    +----------- Linker --------+
                                  |
                           FARX Packages
```

## Current implementation
`MainActivity` hosts the editor and console. `FarxEngine` is a small, dependency-free interpreter slice used to prove the Android build and test pipeline. It is deliberately isolated so the full compiler can replace it without rewriting the UI.

## Planned module boundaries
- `lexer`: tokenization, source spans, diagnostics.
- `parser`: grammar, AST construction and recovery.
- `semantic`: scopes, symbols, type checks and access checks.
- `ir`: intermediate representation.
- `optimizer`: safe, testable optimization passes.
- `runtime`: values, execution context and standard library.
- `linker`: dependency graph, version and compatibility checks.
- `packages`: package manifest, resolver and lock file.
- `studio`: editor, explorer, terminal, debugger and preview.
