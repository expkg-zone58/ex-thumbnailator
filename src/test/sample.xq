import module namespace t="expkg-zone58:image.thumbnailator";

declare variable $pic1:=resolve-uri("resources/simple.jpg");
 declare variable $picr:=resolve-uri("resources/result2.jpg");
 declare variable $remote:="http://images.metmuseum.org/CRDImages/ep/original/DT46.jpg";
 declare variable $watermark:="C:\Users\andy\git\ex-thumbnailator\src\test\resources\icon.jpg";
let $task:=<thumbnail>
              <size width="500" height="500"/>
              <filters>             
              <colorize color="#FF0096" alpha=".4"/>      
              <caption position="CENTER"  color="white" size="30" font="SansSerif">Some Text here</caption>
              
               <rotate angle="15"/>
                <canvas height="500" width="500" position="TOP_LEFT" color="black"/> 
                <watermark src="{$watermark}" alpha=".8"  position="TOP_LEFT"/>  
              </filters>         
            </thumbnail>
(: let $task:=doc("rotate.xml")/task :)
let $x:=fetch:binary($remote)
 let $r:=Q{java:org.expkgzone58.image.Thumbs}size($x,xs:int(40),xs:int(40))
(: let $r:=Q{java:org.expkgzone58.image.Thumbs}task($x,$task) :) 
return file:write-binary($picr,$r)