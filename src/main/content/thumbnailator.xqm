xquery version "3.0" encoding "UTF-8";
(:~ Generate thumbnails using http://code.google.com/p/thumbnailator/
 : @author andy bunce
 :)
module namespace thumbnails = 'apb.image.thumbnailator';
declare default function namespace 'apb.image.thumbnailator'; 

declare namespace File="java:java.io.File";
declare namespace Thumbnailator="java:net.coobird.thumbnailator.Thumbnailator";
declare namespace BufferedImageBuilder="net.coobird.thumbnailator.builders.BufferedImageBuilder";
(:
Thumbnails.of(new File("path/to/directory").listFiles())
        .size(640, 480)
        .outputFormat("jpg")
        .toFiles(Rename.PREFIX_DOT_THUMBNAIL);
:)
declare function fromFilenames($file as xs:string){
  thumbnails:fromFilenames($file)
}; 

declare function size($builder,$width as xs:int,$height as xs:int){
  BufferedImageBuilder:size($builder,$width,$height)
};  

declare function make($src as xs:string,
             $dest as xs:string,
             $width as xs:integer,
             $height as xs:integer){
    let $src:=File:new($src)
    let $dest:=File:new($dest) 
    return Thumbnailator:createThumbnail($src,$dest,xs:int($width),xs:int($height))              
};         