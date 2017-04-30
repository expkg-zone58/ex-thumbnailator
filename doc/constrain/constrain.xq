(:~ 
 :examples of constrain use
:)
import module namespace t="expkg-zone58:image.thumbnailator";
declare variable $file-base:=file:parent(static-base-uri());

declare variable $src:=file:resolve-path("A17057.jpg",$file-base);
declare function local:wi($data as xs:base64Binary,$filename as xs:string)
{
  file:write-binary(file:resolve-path($filename,$file-base),$data)
};
declare function local:constrain($aspect as xs:boolean,$fit as xs:boolean,$exif as xs:boolean)
{
<thumbnail>
  <size width="60" height="200"/>  
  <constrain aspect="{$aspect}" fit="{$fit}" exif="{$exif}"/>
</thumbnail>
};
let $s:=function($b){if($b) then ".+" else ".-"}
let $img:= fetch:binary($src)
let $ft:=(false(),true())
for $fit in $ft,$aspect in $ft,$exif in $ft
let $file:=("out.",
           $s($aspect) , "aspect",
           $s($fit), "fit",        
           $s($exif), "exif",
            ".jpg")=>string-join("")
let $task:=local:constrain($aspect,$fit,$exif)
return t:task($img,$task) => local:wi($file)
