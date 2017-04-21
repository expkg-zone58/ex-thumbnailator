package org.expkgzone58.image;

import java.awt.Color;
import java.lang.reflect.Field;

import org.basex.query.QueryException;
import org.basex.query.value.item.Bln;
import org.basex.query.value.item.Dbl;
import org.basex.query.value.item.Int;
import org.basex.query.value.node.ANode;
import org.basex.util.Token;

import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

public final class Utils {
    private Utils() {
    };
    // get value from attribute with default
    public static String attrib(final ANode element, final String name, final String def)
            throws QueryException {
        byte[] at = element.attribute(name);
        return (at == null) ? def : Token.string(at);
    }

    public static float attrib(final ANode element, final String name, final float def)
            throws QueryException {
        byte[] at = element.attribute(name);
        return (at == null) ? def : (float) Dbl.parse(at, null);
    }

    public static int attrib(final ANode element, final String name, final int def)
            throws QueryException {
        byte[] at = element.attribute(name);
        return (at == null) ? def : (int) Int.parse(at, null);
    }

    public static boolean attrib(final ANode element, final String name, final boolean def)
            throws QueryException {
        byte[] at = element.attribute(name);
        return (at == null) ? def : (boolean) Bln.parse(at, null);
    }

    // e.g. TOP_LEFT
    public static Position position(final ANode element, final String name, final Position def) {
        byte[] at = element.attribute(name);
        return (at == null) ? def : Positions.valueOf(Token.string(at));
    }

    public static Color stringToColor(final String value) {
        if (value == null) {
            return Color.black;
        }
        try {
            // get color by hex or octal value
            return Color.decode(value);
        } catch (NumberFormatException nfe) {
            // if we can't decode lets try to get it by name
            try {
                // try to get a color by name using reflection
                final Field f = Color.class.getField(value);

                return (Color) f.get(null);
            } catch (Exception ce) {
                // if we can't get any color return black
                return Color.black;
            }
        }
    }

}
