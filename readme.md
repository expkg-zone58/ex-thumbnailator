# expkg-zone58:image.thumbnailator

An XQuery interface to the image thumbnail generator library
[thumbnailator](https://github.com/coobird/thumbnailator) for BaseX 9.4.5+.

Features size, scale, caption, rotate, flip, colorize, watermark. More details at [doc](doc/readme.md)

## Versions
* thumbnailator 8 requires Basex 9.4.5+
* uses thumbnailator 4.13
## Usage

Images are input and output as `xs:base64Binary` items. Inputs typically come from `fetch:binary`
which allows for file and http sources. Outputs may be saved with `file:write-binary` or `db:store`

### Simple

Create a thumbnail of given size
```xquery
import module namespace t="expkg-zone58:image.thumbnailator";

fetch:binary("http://images.metmuseum.org/CRDImages/ep/original/DT46.jpg")
=>t:size(80)
```
or scale to a fraction of the original

```xquery
import module namespace t="expkg-zone58:image.thumbnailator";

fetch:binary("file:///Z:/recordings/radio/Book%20of%20the%20Week/image.png")
=>t:scale(0.25)
```

### Tasks
This function takes an XML description of the operations to apply to generate the thumbnail.

```xquery
import module namespace t="expkg-zone58:image.thumbnailator";
declare variable $watermark:="C:\Users\andy\git\ex-thumbnailator\src\test\resources\icon.jpg";

let $task:=
<thumbnail>
    <size width="100" height="100"/>
    <filters>             
        <colorize color="green" alpha=".5"/>      
        <caption position="CENTER">Some Text here</caption>
        <rotate angle="15"/>
        <canvas height="500" width="500" position="TOP_LEFT" color="black"/> 
        <watermark src="{$watermark}" alpha=".5"  position="TOP_LEFT"/>  
    </filters>         
</thumbnail>

return fetch:binary("http://images.metmuseum.org/CRDImages/ep/original/DT46.jpg")
=>t:task($task)
```
The schema for this XML is available at [task.xsd](./src/main/content/task.xsd)

## Installation
The library is packaged in the [EXpath](http://expath.org/spec/pkg) xar format with 
the thumbnailator jar included. See [releases](../../releases) for installation instructions.

# Tests
`test.xqm` script uses the BaseX [Unit module](http://docs.basex.org/wiki/Unit_Module)

## License

* ex-thumbnailator Copyright (c) 2013-2017, Andy Bunce. (Apache 2 License). 
* thumbnailator Copyright (c) Chris Kroells (MIT License).



