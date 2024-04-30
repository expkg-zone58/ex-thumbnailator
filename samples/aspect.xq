(:~ 
 :examples of constrain use
 : generate 8 files showing all perms of aspect,fit,exif
:)
import module namespace t="expkg-zone58:image.thumbnailator";
declare variable $resources:=file:resolve-path("../src/test/resources/.",file:base-dir()) ;
declare variable $watermark:=file:resolve-path("icon.gif",$resources);
declare variable $src:=file:resolve-path("A34283.jpg",$resources);

declare function local:wi($data as xs:base64Binary,$filename as xs:string)
{
  file:write-binary(file:resolve-path($filename=>trace("AA"),file:base-dir()),$data)
};

declare function local:constrain($aspect as xs:boolean,$fit as xs:boolean,$exif as xs:boolean)
{
  <constrain aspect="{$aspect}" fit="{$fit}" exif="{$exif}"/>
};

let $s:=function($b){if($b) then ".+" else ".-"}
let $img:= fetch:binary($src)
let $ft:=(false(),true())
for $fit in $ft,$aspect in $ft,$exif in $ft
let $file:=("out",
           $s($aspect) , "aspect",
           $s($fit), "fit",        
           $s($exif), "exif")=>string-join("")
let $task:=<thumbnail>
              <size width="250" height="250"/>
              {local:constrain($aspect,$fit,$exif)}
              <filters>
                <canvas height="300" width="300" color="lightGray" position="CENTER"/>
                <caption size="20" color="black" position="TOP_CENTER">{$file}</caption>
                <watermark src="{$watermark}" alpha="1"  position="BOTTOM_RIGHT"/> 
              </filters>
              <output format="gif"/>
             </thumbnail>           
return t:task($img,$task) => local:wi("outputs/" || $file || ".gif")
