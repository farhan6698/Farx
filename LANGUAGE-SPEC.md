# FARX Language Specification — Preview 0.2

## Design goals
- Independent language and ecosystem named FARX.
- File extension: `.farx`.
- Case-insensitive keywords.
- No indentation-sensitive syntax.
- English-first keywords; German aliases may be added only with deterministic, documented mappings.
- Abbreviations must be unambiguous and at most six characters.
- Explicit block delimiters: `MODE ...` and `END MODE`.

## Implemented preview grammar
```ebnf
program      = { comment | mode_block } ;
mode_block   = "MODE", identifier, newline, { statement }, "END MODE" ;
statement    = print_stmt | let_stmt | arithmetic_stmt | link_stmt ;
print_stmt   = "PRINT", ( string | number | identifier ) ;
let_stmt     = "LET", identifier, "=", number ;
arithmetic_stmt = ("ADD" | "SUB" | "MUL" | "DIV"), identifier, identifier ;
link_stmt    = "LINK", identifier ;
comment      = "#", { any_character } | "//", { any_character } ;
```

## Supported commands in this build
- `MODE CODE` / `END MODE`: starts and ends a block.
- `PRINT "text"`: writes a string, number or variable name.
- `LET name = number`: defines a numeric variable.
- `ADD a b`, `SUB a b`, `MUL a b`, `DIV a b`: performs arithmetic and prints the result.
- `LINK module`: parses a module reference and reports that it is not loaded yet.

Keywords are case-insensitive. Variable names are normalized to lowercase. Arithmetic currently supports numeric literals in LET declarations and previously declared variables as operands.

## Current limitations
- The parser is a compact line-oriented preview, not yet a token-stream Lexer + AST Parser.
- No functions, loops, conditionals, arrays, objects, user-defined types or file imports.
- LINK is syntax recognition only; no actual linker/module loading.
- Mode names beyond the small supported set are not executable.
- The app currently provides a basic editor and console, not a full IDE.

## Error handling
The preview reports line numbers for missing block delimiters, unknown commands, undefined variables and division by zero.
