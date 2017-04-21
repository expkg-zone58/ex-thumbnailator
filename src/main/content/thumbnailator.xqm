xquery version "3.1" encoding "UTF-8";
(:~ Generate image thumbnails using the thumbnailator library. 
 : @see https://github.com/coobird/thumbnailator
 : @author andy bunce
 : @version 4.4.1
 :)
module namespace thumbnails = 'expkg-zone58:image.thumbnailator';

(:~
 : generate scaled version of source image with maximum dimension of size
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @result base64Binary for thumbnail
 :)
declare function thumbnails:size($source  as xs:base64Binary,$width as xs:integer,$height as xs:integer)
as  xs:base64Binary
{
  Q{java:org.expkgzone58.image.Thumbs}size($source,xs:int($width),xs:int($height))
};
(:~
 : generate scaled version of source image at given factors 0-1
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @result base64Binary for thumbnail
 :)
declare function thumbnails:scale($source  as xs:base64Binary,$xscale as xs:float,$yscale as xs:float)
as  xs:base64Binary
{
  Q{java:org.expkgzone58.image.Thumbs}scale($source,$xscale,$yscale)
};

(:~
 : generate thumbnail using parameters specified via XML
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @param $task XML parameters <task><size width="100" ..
 : @result base64Binary for thumbnail
 :)
declare function thumbnails:task($source  as xs:base64Binary,
                                 $task as element(thumbnail))
as  xs:base64Binary
{
  Q{java:org.expkgzone58.image.Thumbs}task($source,$task)
};

(:~
 : validate task XML against schema
 : @param $src XML parameters <task><size width="100" ..
 : @result validation report
 :)
declare function thumbnails:validation-report($src)
as element(report)
{
  validate:xsd-report($src,"task.xsd")
};