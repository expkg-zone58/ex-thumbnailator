module namespace test = 'http://basex.org/modules/xqunit-tests';
(:~
 : unit tests for thumbnail module
 :)
import module namespace t="expkg-zone58:image.thumbnailator" ;

declare variable $test:pic1:=resolve-uri(  "resources/simple.jpg");
declare variable $test:picr:=resolve-uri(  "resources/out.jpg");



 
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
  
(:~ validate a task :)
declare
  %unit:test
  function test:validate() {
  let $r:=t:validation-report(doc("rotate.xml"))

  return unit:assert( $r )
  
};