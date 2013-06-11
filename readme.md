# apb.image.thumbnailator
An XQuery interface to the thumbnail generator library
[thumbnailator](http://code.google.com/p/thumbnailator/)  

BaseX 7.7 or greater is required.


# Installation
The library is packaged in the [EXpath](http://expath.org/spec/pkg) xar format with 
the thumbnailator jar included. It can be installed into the BaseX repository by 
executing the command:
````
repo:install('http://apb2006.github.io/thumbnailator/dist/thumbnailator.zar')
````
# Usage
````
import module namespace t="apb.image.thumbnailator";
t:make("/tmp/pic.jpg","/tmp/small.jpg",120,120)
````
# Tests
The `test.xq` script uses the BaseX [Unit module](http://docs.basex.org/wiki/Unit_Module)
todo
# 
explore java binding limits and fluent interface 