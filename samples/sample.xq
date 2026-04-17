import module namespace t="expkg-zone58:image.thumbnailator" ;
declare variable $resources:=file:resolve-path("../src/test/resources/.",file:base-dir()) ;



 declare variable $picr:=file:resolve-path("result2.jpg",$resources);
 declare variable $remote:="https://upload.wikimedia.org/wikipedia/commons/3/34/Art-portrait-collage_2.jpg";
 declare variable $watermark:=file:resolve-path("icon.jpg",$resources);
let $task:=<thumbnail>
              <size width="500" height="500"/>
              <filters>             
              <colorize color="#FF0096" alpha=".4"/>      
              <caption position="CENTER"  color="white" size="30" font="SansSerif">Some Text here</caption>
              
               <rotate angle="15"/>
                <canvas height="500" width="500" position="TOP_LEFT" color="black"/> 
                <watermark src="{$watermark}" alpha=".8"  position="BOTTOM_LEFT"/>  
              </filters>         
            </thumbnail>
(: let $task:=doc("rotate.xml")/task :)
let $x:=fetch:binary($remote)
 let $r:=t:task($x,$task)
(: let $r:=Q{java:org.expkgzone58.image.Thumbs}task($x,$task) :) 
return file:write-binary($picr,$r)