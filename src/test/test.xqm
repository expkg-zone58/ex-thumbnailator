module namespace test = 'http://basex.org/modules/xqunit-tests';
(:~
 : unit tests for thumbnail module
 :)
import module namespace t="expkg-zone58:image.thumbnailator" (: at "../main/content/thumbnailator.xqm" :);

declare variable $test:pic1:=resolve-uri(  "resources/simple.jpg");
declare variable $test:picr:=resolve-uri(  "resources/out.jpg");
declare variable $test:met:=<images>
 <image src="http://images.metmuseum.org/CRDImages/rl/web-large/DT3136.jpg">
 http://www.metmuseum.org/art/collection/search/459117
 </image>
 </images>;


 
(:~ make a thumbnail :)
declare
  %unit:test
  function test:size() {
  let $r:=t:size(
    fetch:binary(
      $test:pic1
    ),60,60
  )
  return unit:assert(
    $r instance of xs:base64Binary
  )
  
};
  
(:~ make a thumbnail :)
declare
  %unit:test
  function test:validate() {
  let $r:=t:validation-report(doc("rotate.xml"))

  return unit:assert( $r )
  
};