# AGENTS.md

## Repo: ex-thumbnailator

XQuery module for BaseX 10+ that wraps the thumbnailator Java library to generate image thumbnails.

## Key facts

- **Language**: XQuery 3.1 + Java (BaseX module)
- **Main module**: `src/main/content/thumbnailator.xqm` → `expkg-zone58:image.thumbnailator`
- **Java class**: `src/java/org/expkgzone58/image/Thumbs.java`
- **Schema**: `src/main/content/task.xsd` for thumbnail task XML

## Build & test

**Build xar package**: Run `tools/build.xq` in BaseX to generate:
- Dist: `dist/thumbnailator-8.0.1.xar`
- XQDoc: `dist/doc/thumbnailator.xqm.xml`
- Updates: `package.xml`

**Run tests**: `test.xqm` uses BaseX Unit module (`%unit:test` functions)

## Core API

| Function | Description |
|----------|-------------|
| `size($src, $w, $h)` | Thumbnail to exact dimensions |
| `scale($src, $x, $y)` | Scale by factor (0-1) |
| `task($src, $task)` | Full control via XML task |

**Inputs/outputs**: `xs:base64Binary` (use `fetch:binary` and `file:write-binary`)

## XML task structure

```xml
<thumbnail>
  <size width="100" height="100"/>  <!-- or <scale x="0.5" y="0.5"/> -->
  <region .../>                     <!-- optional crop -->
  <constrain aspect="true" fit="true" exif="true"/>  <!-- default true -->
  <filters>...</filters>             <!-- colorize, caption, rotate, flip, canvas, watermark -->
  <output format="gif"/>            <!-- override output format -->
</thumbnail>
```

## Exif gotcha

When `constrain@exif="true"` (default):
- `size` and `fit` dimensions swap for portrait images
- `flip` filter is ignored

Set `exif="false"` to disable.

## Project structure

```
src/
  main/
    content/        # XQuery modules, XSD, JARs
    basex.xml       # Package metadata (JARs, Java class)
    expath-pkg.xml  # EXPath package def
  test/
    test.xqm        # Unit tests
    sample.xq       # Usage examples
    *.xml           # Test tasks
```

## Setup

- Requires BaseX 10+
- Java 17
- Classpath: `lib/thumbnailator-0.4.20.jar`, `lib/BaseX*.jar`
