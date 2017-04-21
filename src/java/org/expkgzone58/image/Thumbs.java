package org.expkgzone58.image;

import static org.basex.query.QueryError.IOERR_X;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.basex.io.IOContent;
import org.basex.query.QueryException;
import org.basex.query.QueryModule;
//import org.basex.query.func.fn.FnTrace;
import org.basex.query.value.item.B64Stream;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Int;
import org.basex.query.value.node.ANode;
import org.basex.util.Token;
import org.basex.data.Data;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.geometry.Size;
import net.coobird.thumbnailator.tasks.StreamThumbnailTask;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.filters.Caption;
import net.coobird.thumbnailator.filters.Colorize;
import net.coobird.thumbnailator.filters.Flip;
import net.coobird.thumbnailator.filters.ImageFilter;

/*
 * BaseX Thumbnailator interface
 * @author andy bunce
 * @copyright Quodatum Ltd
 * @date 2017
 * @licence Apache 2
 */
public class Thumbs extends QueryModule{

    public static B64Stream size(final B64Stream inputStream, final int width, final int height)
            throws IOException, QueryException {
        ByteArrayInputStream is = new ByteArrayInputStream(inputStream.binary(null));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ThumbnailParameterBuilder builder = new ThumbnailParameterBuilder();
        builder.size(new Dimension(width, height));
        StreamThumbnailTask task = new StreamThumbnailTask(builder.build(), is, os);
        Thumbnailator.createThumbnail(task);
        return new B64Stream(new IOContent(os.toByteArray()), IOERR_X);
    }

    public static B64Stream scale(final B64Stream inputStream,
            final double xscale, final double yscale)
            throws IOException, QueryException {
        ByteArrayInputStream is = new ByteArrayInputStream(inputStream.binary(null));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ThumbnailParameterBuilder builder = new ThumbnailParameterBuilder();
        builder.scale(xscale, yscale);
        StreamThumbnailTask task = new StreamThumbnailTask(builder.build(), is, os);
        Thumbnailator.createThumbnail(task);
        return new B64Stream(new IOContent(os.toByteArray()), IOERR_X);
    }

    public static B64Stream task(final B64Stream inputStream, final ANode thumbnail)
            throws IOException, QueryException {
        ByteArrayInputStream is = new ByteArrayInputStream(inputStream.binary(null));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ThumbnailParameter param = fromNode(thumbnail);
        StreamThumbnailTask task = new StreamThumbnailTask(param, is, os);
        Thumbnailator.createThumbnail(task);
        return new B64Stream(new IOContent(os.toByteArray()), IOERR_X);
    }

    // build parameters from XML
    static ThumbnailParameter fromNode(final ANode node) throws QueryException, IOException {
        ThumbnailParameterBuilder builder = new ThumbnailParameterBuilder();
        Iterator<ANode> itr = node.children().iterator();
        while (itr.hasNext()) {
            ANode element = itr.next();
            if (element.kind() == Data.ELEM) {
                String name = Token.string(element.name());
                switch (name) {

                case "size":
                   size(builder, element);
                    break;

                case "scale":
                    scale(builder, element);
                     break;

                case "region":
                    region(builder, element);
                    break;

                case "exif-orientation":
                    exif(builder, element);
                    break;

                case "filters":
                    List<ImageFilter> filters = filters(element);
                    builder.filters(filters);
                    break;

                default:
                    break;
                }
            }
        }
        return builder.build();
    }

    static void region(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {

        int width = (int) Int.parse(node.attribute("width"), null);
        int height = (int) Int.parse(node.attribute("height"), null);
        Dimension d = new Dimension(width, height);
        Position pos = Utils.position(node, "position", Positions.CENTER);
        Region r = new Region(pos, (Size) d);
        builder.region(r);
    }
    static void exif(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {
        boolean use = Utils.attrib(node, "use", true);
        builder.useExifOrientation(use);
    }

    static void size(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {
        int width = (int) Int.parse(node.attribute("width"), null);
        int height = (int) Int.parse(node.attribute("height"), null);
        builder.size(width, height);
    }

    static void scale(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {
        double x = Utils.attrib(node, "x", 0.5f);
        double y = Utils.attrib(node, "y", 0.5f);
        builder.scale(x, y);
    }

    static List<ImageFilter> filters(final ANode filters) throws QueryException, IOException {
        Pipeline pipeline = new Pipeline();
        Iterator<ANode> itr = filters.children().iterator();
        while (itr.hasNext()) {
            ANode node = itr.next();
            if (node.kind() == Data.ELEM) {
                switch (Token.string(node.name())) {

                case "canvas": // @width @height @color @position
                    canvas(pipeline, node);
                    break;

                case "caption": //@color @position text()
                    caption(pipeline, node);
                    break;

                case "colorize": //@color @alpha
                    colorize(pipeline, node);
                    break;

                case "flip": //@axis
                    flip(pipeline, node);
                    break;

                case "rotate": //@angle
                    rotate(pipeline, node);
                    break;

                case "watermark": //@src @alpha @position
                    watermark(pipeline, node);
                    break;

                default:
                    break;
                }
            }
        }
        return pipeline.getFilters();
    }

    private static void watermark(final Pipeline pipeline, final ANode node)
            throws IOException, QueryException {
        ImageFilter filter;
        Position pos;
        String src = Token.string(node.attribute("src"));
        pos = Utils.position(node, "position", Positions.BOTTOM_RIGHT);
        BufferedImage watermarkImg = ImageIO.read(new File(src));
        filter = new Watermark(pos, watermarkImg, Utils.attrib(node, "alpha", 0.5f));
        pipeline.add(filter);
    }

    private static void rotate(final Pipeline pipeline, final ANode node)
            throws QueryException {
        double angle = (double) Dbl.parse(node.attribute("angle"), null);
        pipeline.add(Rotation.newRotator(angle));
    }

    private static void flip(final Pipeline pipeline, final ANode node) {
        ImageFilter filter;
        String axis = Token.string(node.attribute("axis"));
               // FnTrace.trace(axis.getBytes(), "FLIP: ".getBytes(), queryContext);
        filter = axis.equalsIgnoreCase("vertical") ? Flip.VERTICAL : Flip.HORIZONTAL;
        pipeline.add(filter);
    }

    private static void colorize(final Pipeline pipeline, final ANode node) throws QueryException {
        ImageFilter filter;
        String color;
        color = Utils.attrib(node, "color", "black");
        filter = new Colorize(Utils.stringToColor(color),
                Utils.attrib(node, "alpha", 0.0f));
        pipeline.add(filter);
    }

    private static void canvas(final Pipeline pipeline, final ANode node) throws QueryException {
        ImageFilter filter;
        String color;
        Position pos;
        int width = (int) Int.parse(node.attribute("width"), null);
        int height = (int) Int.parse(node.attribute("height"), null);
        color = Utils.attrib(node, "color", "black");
        pos = Utils.position(node, "position", Positions.CENTER);
        filter = new Canvas(width, height, pos, false, Utils.stringToColor(color));
        pipeline.add(filter);
    }

    private static void caption(final Pipeline pipeline, final ANode node) throws QueryException {
        ImageFilter filter;
        String color;
        Position pos;
        String text = Token.string(node.string());
        color = Utils.attrib(node, "color", "black");
        pos = Utils.position(node, "position", Positions.TOP_CENTER);
        String fontName = Utils.attrib(node, "font", "SansSerif");
        int size = Utils.attrib(node, "size", 14);
        Font font = new Font(fontName, Font.PLAIN, size);
        filter = new Caption(text, font , Utils.stringToColor(color),
                pos, 0);
        pipeline.add(filter);
    }
}
