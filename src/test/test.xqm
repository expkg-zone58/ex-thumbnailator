module namespace test = 'http://basex.org/modules/xqunit-tests';
(:~
 : unit tests for thumbnail module
 :)
import module namespace t="expkg-zone58.image.thumbnailator" at "../main/content/thumbnailator.xqm";
declare namespace File="java:java.io.File";

declare variable $test:pic1:=resolve-uri("simple.jpg");
 declare variable $test:picr:=resolve-uri("out.jpg");


 
(:~ make a thumbnail :)
declare
  %unit:test
  function test:create-thumbnail() {
  let $r:=t:create-thumbnail($test:pic1,$test:picr,60,60)
  return unit:assert(fn:empty($r))
  
};
  
