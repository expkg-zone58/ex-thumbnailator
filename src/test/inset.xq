(:~ 
 :examples of inset use
:)
import module namespace t="expkg-zone58:image.thumbnailator";

declare variable $watermark:=local:path("resources\icon.gif");
declare variable $src:=local:path("resources/A34283.jpg");

declare function local:path($name){
  file:resolve-path($name,file:base-dir())
};
declare function local:save($data as xs:base64Binary,$filename as xs:string)
{
  file:write-binary(local:path($filename),$data)
};


let $img:= fetch:binary($src)

let $task:=<thumbnail>
              <size width="300" height="300"/>
              <constrain fit="true"/>
              <filters>
               <rotate angle="45"/>
                <caption size="60" color="white"  position="CENTER">DRAFT</caption>
                <rotate angle="-45"/>
                <canvas height="300" width="300" color="lightGray" position="CENTER"/>
                <caption size="30" color="black" insets="10"
                position="TOP_CENTER">Caption insets = 10</caption>
                <watermark src="{$watermark}" alpha="1"  position="BOTTOM_RIGHT"/>
                <caption size="30" color="black" insets="-3"
                position="BOTTOM_LEFT">BOTTOM_LEFT -3</caption> 
               
              </filters>
              <output format="gif"/>
             </thumbnail>           
return t:task($img,$task) => local:save("resources/" ||  "inset.gif")
