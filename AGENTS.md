# AGENTS.md

## Repo: ex-thumbnailator

XQuery module for BaseX 10+ wrapping thumbnailator Java library.

## Quick start

- **Build**: `tools/build.xq` → `dist/thumbnailator-8.0.1.xar`
- **Test**: `src/test/test.xqm` (`%unit:test` functions)
- **Sample**: `src/test/sample.xq`

## API

| Function | Returns |
|----------|---------|
| `thumbnails:size($src, $w, $h)` | `xs:base64Binary` |
| `thumbnails:scale($src, $x, $y)` | `xs:base64Binary` |
| `thumbnails:task($src, $task)` | `xs:base64Binary` |
| `thumbnails:validate($src)` | `empty-sequence()` or error |
| `thumbnails:validation-report($src)` | `element(report)` |

Inputs from `fetch:binary()`, outputs via `file:write-binary()`.

## XML schema

- Root: `<thumbnail>` (not `<task>`)
- Either `<size>` or `<scale>` required
- `constrain@exif="true"` default: swaps dimensions for portrait, ignores `flip`

## Structure

```
src/main/           # XQuery, JARs, XSD, basex.xml, expath-pkg.xml
src/java/           # Thumbs.java (BaseX QueryModule)
src/test/           # test.xqm, sample.xq, test tasks
tools/              # build.xq
dist/               # built xar package
```

## Setup

- BaseX 10+, Java 17
- JARs: `thumbnailator-0.4.13.jar`, `thumbhelper-8.0.0.jar`
