xquery version "3.1" encoding "UTF-8";
(:~ Generate image thumbnails using the thumbnailator library. 
 : @see https://github.com/coobird/thumbnailator
 : @author andy bunce
 : @version 0.6 
 :)
module namespace thumbnails = 'expkg-zone58:image.thumbnailator';
import module namespace Thumbs = "org.expkgzone58.image.Thumbs";

(:~
 : generate scaled version of source image with maximum dimension of size
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @return base64Binary for thumbnail
 :)
declare function thumbnails:size($source  as xs:base64Binary,$size as xs:integer)
as  xs:base64Binary
{
  Thumbs:size($source,xs:int($size),xs:int($size))
};

(:~
 : generate scaled version of source image with maximum dimension of size
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @return base64Binary for thumbnail
 :)
declare function thumbnails:size($source  as xs:base64Binary,$width as xs:integer,$height as xs:integer)
as  xs:base64Binary
{
  Thumbs:size($source,xs:int($width),xs:int($height))
};
(:~
 : generate scaled version of source image at given factors 0-1
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @return the thumbnail
 :)
declare function thumbnails:scale($source  as xs:base64Binary,$scale as xs:double)
as  xs:base64Binary
{
  Thumbs:scale($source,$scale,$scale)
};

(:~
 : generate scaled version of source image at given factors 0-1
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @return the thumbnail
 :)
declare function thumbnails:scale($source  as xs:base64Binary,$xscale as xs:double,$yscale as xs:double)
as  xs:base64Binary
{
  Thumbs:scale($source,$xscale,$yscale)
};
(:~
 : generate thumbnail using parameters specified via XML
 : @param $source base64Binary (streamed?) e.g from `fetch:binary`
 : @param $task XML parameters <task><size width="100" ..
 : @return the thumbnail
 :)
declare function thumbnails:task($source  as xs:base64Binary,
                                 $task as element(thumbnail))
as  xs:base64Binary
{
  Thumbs:task($source,$task)
};

(:~
 : validate task thumbnail XML against schema
 : @param $src XML parameters <thumbnail><size width="100" ..
 : @return empty-sequence or error
 : @error BXVA0001: the validation fails.
 : @error BXVA0002: the validation process cannot be started.
 : @error BXVA0003: no XML Schema validator is available.
 : @error BXVA0004: no validator is found for the specified version. 
 :)
declare function thumbnails:validate($src)
as empty-sequence()
{
  validate:xsd($src,"task.xsd")
};
(:~
 : validate task thumbnail XML against schema
 : @param $src XML parameters <thumbnail><size width="100" ..
 : @return validation report
 :)
declare function thumbnails:validation-report($src)
as element(report)
{
  validate:xsd-report($src,"task.xsd")
};

declare function thumbnails:schema-uri()
as xs:anyURI
{
  resolve-uri("task.xsd")
};