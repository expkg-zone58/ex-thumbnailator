# expkg-zone58.image.thumbnailator
An XQuery interface to the thumbnail generator library
[thumbnailator](https://github.com/coobird/thumbnailator)  

Tested against BaseX 8.4.3.


# Installation
The library is packaged in the [EXpath](http://expath.org/spec/pkg) xar format with 
the thumbnailator jar included. It can be installed into the BaseX repository by 
executing the command:
````
repo:install('https://github.com/expkg-zone58/ex-thumbnailator/dist/thumbnailator.zar')
````
# Usage
````
import module namespace t="expkg-zone58.image.thumbnailator";
t:create-thumbnail("/tmp/pic.jpg","/tmp/small.jpg",120,120)
````
# Tests
`test.xqm` script uses the BaseX [Unit module](http://docs.basex.org/wiki/Unit_Module)

# todo

explore java binding limits and fluent interface 