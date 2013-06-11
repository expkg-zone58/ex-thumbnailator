xquery version "3.0" encoding "UTF-8";
(:~ Generate thumbnails using http://code.google.com/p/thumbnailator/
 : @author andy bunce
 :)
module namespace thumbnails = 'apb.image.thumbnailator';
declare default function namespace 'apb.image.thumbnailator'; 

declare namespace File="java:java.io.File";
declare namespace Thumbnailator="java:net.coobird.thumbnailator.Thumbnailator";
declare namespace BufferedImageBuilder="java:net.coobird.thumbnailator.builders.BufferedImageBuilder";
declare namespace Builder="java:net.coobird.thumbnailator.Thumbnails$Builder";
(:
Thumbnails.of(new File("path/to/directory").listFiles())
        .size(640, 480)
        .outputFormat("jpg")
        .toFiles(Rename.PREFIX_DOT_THUMBNAIL);
:)
declare function fromFilenames($file as xs:string){
  let $a:=File:new(file:path-to-native($file))
  return Builder:of(($a,$a))
}; 

declare function sourceRegion($x as xs:integer,$y as xs:integer,
$width as xs:integer,$height as xs:integer){
  Builder:sourceRegion(xs:int($x),xs:int($y),xs:int($width),xs:int($height))
}; 

declare function size($builder,$width as xs:int,$height as xs:int){
  BufferedImageBuilder:size($builder,$width,$height)
};  

declare function make($src as xs:string,
             $dest as xs:string,
             $width as xs:integer,
             $height as xs:integer){
    let $src:=File:new(file:path-to-native($src))
    let $dest:=File:new(file:path-to-native($dest)) 
    return Thumbnailator:createThumbnail($src,$dest,xs:int($width),xs:int($height))              
};         