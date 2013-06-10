(: create o/ps
 : xar package and xqdoc
 :)

 declare function local:read($item){
  let $f:=fn:resolve-uri($item/@src)
  return  file:read-binary($f)
 };
 
 let $pack:=doc(resolve-uri("package.xml"))/*
let $files:=$pack/*
let $zip   := archive:create( $files/@dest/fn:string(), $files ! local:read(.)) 
return ( file:write-binary(resolve-uri($pack/@dest),$zip)
         ,file:write(resolve-uri("dist/xqdoc.xml"),inspect:xqdoc(resolve-uri("src/main/content/thumbnailator.xqm")))
     
)
