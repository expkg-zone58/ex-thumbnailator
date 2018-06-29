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

import org.basex.data.Data;
import org.basex.io.IOContent;
import org.basex.query.QueryException;
import org.basex.query.QueryModule;
//import org.basex.query.func.fn.FnTrace;
import org.basex.query.value.item.B64Lazy;
import org.basex.query.value.node.ANode;
import org.basex.util.Token;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.filters.Caption;
import net.coobird.thumbnailator.filters.Colorize;
import net.coobird.thumbnailator.filters.Flip;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.tasks.StreamThumbnailTask;

/*
 * BaseX Thumbnailator interface
 * @author andy bunce
 * @copyright Quodatum Ltd
 * @date 2017
 * @licence Apache 2
 */
public class Thumbs extends QueryModule{

    public B64Lazy size(final B64Lazy inputStream, final int width, final int height)
            throws IOException, QueryException {
        ByteArrayInputStream is = new ByteArrayInputStream(inputStream.binary(null));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ThumbnailParameterBuilder builder = new ThumbnailParameterBuilder();
        builder.size(width, height);
        StreamThumbnailTask task = new StreamThumbnailTask(builder.build(), is, os);
        Thumbnailator.createThumbnail(task);
        return new B64Lazy(new IOContent(os.toByteArray()), IOERR_X);
    }

    public B64Lazy scale(final B64Lazy inputStream,
            final double xscale, final double yscale)
            throws IOException, QueryException {
        ByteArrayInputStream is = new ByteArrayInputStream(inputStream.binary(null));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ThumbnailParameterBuilder builder = new ThumbnailParameterBuilder();
        builder.scale(xscale, yscale);
        StreamThumbnailTask task = new StreamThumbnailTask(builder.build(), is, os);
        Thumbnailator.createThumbnail(task);
        return new B64Lazy(new IOContent(os.toByteArray()), IOERR_X);
    }

    public B64Lazy task(final B64Lazy inputStream, final ANode thumbnail)
            throws IOException, QueryException {
        ByteArrayInputStream is = new ByteArrayInputStream(inputStream.binary(null));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ThumbnailParameter param = fromNode(thumbnail);
        StreamThumbnailTask task = new StreamThumbnailTask(param, is, os);
        Thumbnailator.createThumbnail(task);
        return new B64Lazy(new IOContent(os.toByteArray()), IOERR_X);
    }

    // build parameters from XML
    ThumbnailParameter fromNode(final ANode node) throws QueryException, IOException {
        ThumbnailParameterBuilder builder = new ThumbnailParameterBuilder();

        Iterator<ANode> itr = node.children().iterator();
        while (itr.hasNext()) {
            ANode element = itr.next();
            if (element.kind() == Data.ELEM) {
                String name = Token.string(element.name());
               // FnTrace.trace(name.getBytes(), "element: ".getBytes(), queryContext);
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

                case "constrain":
                    constrain(builder, element);
                    break;

                case "filters":
                    List<ImageFilter> filters = filters(element);
                    builder.filters(filters);
                    break;

                case "output":
                    String format = Utils.attrib(element, "format",
                            ThumbnailParameter.ORIGINAL_FORMAT);
                    builder.format(format);

                    break;
                default:
                    break;
                }
            }
        }
        return builder.build();
    }

    void region(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {

        int width = Utils.attrib(node,"width", 80);
        int height = Utils.attrib(node,"height", 80);
        Dimension d = new Dimension(width, height);
        Position pos = Utils.position(node, "position", Positions.CENTER);
        Region r = new Region(pos, new AbsoluteSize(d));
        builder.region(r);
    }

    void constrain(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {

        boolean aspect = Utils.attrib(node, "aspect", true);
        builder.keepAspectRatio(aspect);
       // FnTrace.trace(Boolean.toString(aspect).getBytes(), "constrain: ".getBytes(), queryContext);
        boolean exif = Utils.attrib(node, "exif", true);
        builder.useExifOrientation(exif);

        boolean fit = Utils.attrib(node, "fit", true);
        builder.fitWithinDimensions(fit);
    }

    void size(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {
        int width = Utils.attrib(node, "width", 80);
        int height = Utils.attrib(node, "height", 80);
        builder.size(width, height);
    }

    void scale(final ThumbnailParameterBuilder builder, final ANode node)
            throws QueryException {
        double x = Utils.attrib(node, "x", 0.5f);
        double y = Utils.attrib(node, "y", 0.5f);
        builder.scale(x, y);
    }

    List<ImageFilter> filters(final ANode filters) throws QueryException, IOException {
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

    private void watermark(final Pipeline pipeline, final ANode node)
            throws IOException, QueryException {
        ImageFilter filter;
        Position pos;
        String src = Utils.attrib(node, "src", "");
        pos = Utils.position(node, "position", Positions.BOTTOM_RIGHT);
        BufferedImage watermarkImg = ImageIO.read(new File(src));
        filter = new Watermark(pos, watermarkImg, Utils.attrib(node, "alpha", 0.5f));
        pipeline.add(filter);
    }

    private void rotate(final Pipeline pipeline, final ANode node)
            throws QueryException {
        double angle = (double) Utils.attrib(node, "angle", 0);
        pipeline.add(Rotation.newRotator(angle));
    }

    private void flip(final Pipeline pipeline, final ANode node) throws QueryException {
        ImageFilter filter;
        String axis = Utils.attrib(node, "axis", "vertical");
               // FnTrace.trace(axis.getBytes(), "FLIP: ".getBytes(), queryContext);
        filter = axis.equalsIgnoreCase("vertical") ? Flip.VERTICAL : Flip.HORIZONTAL;
        pipeline.add(filter);
    }

    private void colorize(final Pipeline pipeline, final ANode node) throws QueryException {
        ImageFilter filter;
        String color;
        color = Utils.attrib(node, "color", "black");
        filter = new Colorize(Utils.stringToColor(color),
                Utils.attrib(node, "alpha", 0.0f));
        pipeline.add(filter);
    }

    private void canvas(final Pipeline pipeline, final ANode node) throws QueryException {
        ImageFilter filter;
        String color;
        Position pos;
        int width = Utils.attrib(node, "width", 80);
        int height = Utils.attrib(node, "height", 80);
        color = Utils.attrib(node, "color", "black");
        pos = Utils.position(node, "position", Positions.CENTER);
        filter = new Canvas(width, height, pos, false, Utils.stringToColor(color));
        pipeline.add(filter);
    }

    private void caption(final Pipeline pipeline, final ANode node) throws QueryException {
        ImageFilter filter;
        String color;
        Position pos;
        String text = Token.string(node.string());
        color = Utils.attrib(node, "color", "black");
        pos = Utils.position(node, "position", Positions.TOP_CENTER);
        String fontName = Utils.attrib(node, "font", "SansSerif");
        int size = Utils.attrib(node, "size", 14);
        int insets = Utils.attrib(node, "insets", 0);
        Font font = new Font(fontName, Font.PLAIN, size);
        filter = new Caption(text, font , Utils.stringToColor(color),
                pos, insets);
        pipeline.add(filter);
    }
}
