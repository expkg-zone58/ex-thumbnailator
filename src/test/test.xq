(:~
 : unit tests for thumbnail module
 :)
import module namespace t="apb.image.thumbnails" at "../main/content/thumbnailator.xqm";

declare variable $pic1:=resolve-uri("simple.jpg");
 declare variable $picr:=resolve-uri("out.jpg");

(:~ Initializing function, which is called once before all tests. :)
declare
  %unit:before-module
  function local:before-all-tests() { ()
};
 
(:~ Initializing function, which is called once after all tests. :)
declare
  %unit:after-module
  function local:after-all-tests() { ()
};
 
(:~ Initializing function, which is called before each test. :)
declare
  %unit:before
  function local:before() { ()
};
 
(:~ Initializing function, which is called after each test. :)
declare
  %unit:after
  function local:after() { ()
};
 
(:~ we get tags :)
declare
  %unit:test
  function local:success-function() {
  unit:assert(t:make($pic1,$picr,60,60))
  
};
  
(: run all tests  :)
unit:test() 