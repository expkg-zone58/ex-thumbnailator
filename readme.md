# expkg-zone58.image.thumbnailator
An XQuery interface to the thumbnail generator library
[thumbnailator](https://github.com/coobird/thumbnailator). 

Tested against BaseX 8.6.2


## Usage

### Simple

```xquery
import module namespace t="expkg-zone58:image.thumbnailator";

fetch:binary("http://images.metmuseum.org/CRDImages/ep/original/DT46.jpg")
=>t:scale(80,60)

```

### Tasks
This function takes an XML description of the operations to apply to generate the thumbnail.

```xml
<thumbnail>
    <size width="500" height="500"/>
    <filters>             
        <colorize color="green" alpha=".5"/>      
        <caption position="CENTER">Some Text here</caption>
        <rotate angle="15"/>
        <canvas height="500" width="500" position="TOP_LEFT" color="black"/> 
        <watermark src="{$watermark}" alpha=".5"  position="TOP_LEFT"/>  
    </filters>         
</thumbnail>
```
A schema is provided.

## Installation
The library is packaged in the [EXpath](http://expath.org/spec/pkg) xar format with 
the thumbnailator jar included. See releases for installation instructions.

# Tests
`test.xqm` script uses the BaseX [Unit module](http://docs.basex.org/wiki/Unit_Module)

## License

* ex-thumbnailator Copyright (c) 2017, Andy Bunce. (Apache 2 License). 
* thumbnailator Copyright (c) Chris Kroells (MIT License).

# todo

scale sourceregion 