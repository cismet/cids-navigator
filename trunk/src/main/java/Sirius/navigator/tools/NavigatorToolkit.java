/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * NavigatorToolkit.java
 *
 * Created on 21. M\u00E4rz 2003, 11:41
 */
package Sirius.navigator.tools;

import java.util.*;

import de.cismet.cismap.commons.interaction.CismapBroker;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class NavigatorToolkit {

    //~ Static fields/initializers ---------------------------------------------

    private static NavigatorToolkit toolkit;

    //~ Instance fields --------------------------------------------------------

    private long current;

    //~ Constructors -----------------------------------------------------------

    /**
     * Singleton constructor, creates a new private instance of MiddlewareToolkit.
     */
    private NavigatorToolkit() {
        current = System.currentTimeMillis();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Gets the singleton shared instance of this toolkit.
     *
     * @return  the singleton shared instance of this toolkit
     */
    public static final NavigatorToolkit getToolkit() {
        if (toolkit == null) {
            toolkit = new NavigatorToolkit();
        }

        return toolkit;
    }

    /**
     * Generates a 'unique' id, that is calculated from the current system time and a random number.
     *
     * @return  a 'unique' id (hexadecimal long value)
     */
    public synchronized String generateId() {
        current += 1;
        return Long.toHexString(current);
    }

    // standard point to String and string to point operations -----------------

    /**
     * (minX, minY, maX, maxY).
     *
     * @param   coordinateString  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public double[] parseBoundingBoxCoordinatesString(String coordinateString) {
        final double[] coordinate = new double[4];

        try {
            coordinateString = coordinateString.substring(coordinateString.indexOf("(") + 1,
                    coordinateString.indexOf(")")); // NOI18N

            final StringTokenizer tokenizer = new StringTokenizer(coordinateString, ","); // NOI18N

            // SICAD Coordinate
            // double minX
            coordinate[0] = new Double(tokenizer.nextToken()).doubleValue();
            // double minY
            coordinate[1] = new Double(tokenizer.nextToken()).doubleValue();
            // double maxX
            coordinate[2] = new Double(tokenizer.nextToken()).doubleValue();
            // double maxY
            coordinate[3] = new Double(tokenizer.nextToken()).doubleValue();
        } catch (Exception exp) {
            coordinate[0] = -1;
            coordinate[1] = -1;
            coordinate[2] = -1;
            coordinate[3] = -1;
        }

        return coordinate;
    }

    /**
     * (minX, minY, maX, maxY).
     *
     * @param   boundingBoxCoordinates  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String toBoundingBoxCoordinatesString(final double[] boundingBoxCoordinates) {
        final StringBuffer buffer = new StringBuffer();

        if ((boundingBoxCoordinates != null) || (boundingBoxCoordinates.length != 0)) {
            buffer.append('(');
            for (int i = 0; i < boundingBoxCoordinates.length; i++) {
                buffer.append(boundingBoxCoordinates[i]);
                if ((i < (boundingBoxCoordinates.length - 1))) {
                    buffer.append(',');
                }
            }
            buffer.append(')');
        }

        return buffer.toString();
    }

    /**
     * String to point coordinates array.
     *
     * @param   pointCoordinateString  String (x,y), ..., e.g. (1,2) (3,4) (5,6)
     *
     * @return  array of point coordintes (x,y)
     */
    public double[][] parsePointCoordinatesString(final String pointCoordinateString) {
        final StringTokenizer tokenizer = new StringTokenizer(pointCoordinateString, "("); // NOI18N
        final double[][] pointCoordinates = new double[tokenizer.countTokens()][2];
        int i = 0;

        while (tokenizer.hasMoreTokens()) {
            final String point = tokenizer.nextToken();
            final String x = point.substring(0, point.indexOf(','));
            final String y = point.substring(x.length() + 1, point.indexOf(')')); // NOI18N

            pointCoordinates[i][0] = Double.parseDouble(x);
            pointCoordinates[i][1] = Double.parseDouble(y);

            i++;
        }

        return pointCoordinates;
    }

    /**
     * Point coordintes array to String.
     *
     * @param   pointCoordinates  array of point coordintes (x,y)
     *
     * @return  String (x,y), ..., e.g. (1,2) (3,4) (5,6)
     */
    public String toPointCoordinatesString(final double[][] pointCoordinates) {
        final StringBuffer buffer = new StringBuffer();

        if ((pointCoordinates != null) || (pointCoordinates.length != 0)) {
            for (int i = 0; i < pointCoordinates.length; i++) {
                buffer.append('(');
                buffer.append(pointCoordinates[i][0]);
                buffer.append(',');
                buffer.append(pointCoordinates[i][1]);
                buffer.append((i < (pointCoordinates.length - 1)) ? ") " : ")"); // NOI18N
            }
        }

        return buffer.toString();
    }

    /**
     * Computes the bounding box of this point coordinates.
     *
     * @param   pointCoordinates  n point corrdintes (x/y)
     *
     * @return  the enclosing bounding box (minX, minY, maxX, maxY);
     */
    public double[] getBoundingBoxCordinates(final double[][] pointCoordinates) {
        final double[] boundingBoxCordinates = new double[4];

        if ((pointCoordinates == null) || (pointCoordinates.length == 0)) {
            return boundingBoxCordinates;
        }

        boundingBoxCordinates[0] = pointCoordinates[0][0];
        boundingBoxCordinates[1] = pointCoordinates[0][1];

        if (pointCoordinates.length < 2) {
            boundingBoxCordinates[2] = pointCoordinates[0][0];
            boundingBoxCordinates[3] = pointCoordinates[0][1];
        } else {
            boundingBoxCordinates[2] = pointCoordinates[1][0];
            boundingBoxCordinates[3] = pointCoordinates[1][1];

            for (int i = 2; i < pointCoordinates.length; i++) {
                // minX
                boundingBoxCordinates[0] = (pointCoordinates[i][0] < boundingBoxCordinates[0])
                    ? pointCoordinates[i][0] : boundingBoxCordinates[0];

                // minY
                boundingBoxCordinates[1] = (pointCoordinates[i][1] < boundingBoxCordinates[1])
                    ? pointCoordinates[i][1] : boundingBoxCordinates[1];

                // maxX
                boundingBoxCordinates[2] = (pointCoordinates[i][0] > boundingBoxCordinates[2])
                    ? pointCoordinates[i][0] : boundingBoxCordinates[2];

                // maxY
                boundingBoxCordinates[3] = (pointCoordinates[i][1] > boundingBoxCordinates[3])
                    ? pointCoordinates[i][1] : boundingBoxCordinates[3];
            }
        }

        return boundingBoxCordinates;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   boundingBoxCordinates  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public double[][] getPointCoordinates(final double[] boundingBoxCordinates) {
        final double[][] pointCoordinates = new double[4][2];

        // x1 y1
        pointCoordinates[0][0] = boundingBoxCordinates[0];
        pointCoordinates[0][1] = boundingBoxCordinates[1];

        // x2 y1
        pointCoordinates[1][0] = boundingBoxCordinates[2];
        pointCoordinates[1][1] = boundingBoxCordinates[1];

        // x2 y2
        pointCoordinates[2][0] = boundingBoxCordinates[2];
        pointCoordinates[2][1] = boundingBoxCordinates[3];

        // x1 y2
        pointCoordinates[3][0] = boundingBoxCordinates[0];
        pointCoordinates[3][1] = boundingBoxCordinates[3];

        return pointCoordinates;
    }

    /**
     * OGC point to String and string to point operations ----------------------
     *
     * @param   pointCoordinates  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String pointCoordinatesToOGCPolygon(final double[][] pointCoordinates) {
        return this.pointCoordinatesToOGCPolygon(pointCoordinates, false);
    }

    /**
     * Point coordintes to OGC Polygon.
     *
     * @param   pointCoordinates  array of point coordintes (x,y)
     * @param   closePolygon      DOCUMENT ME!
     *
     * @return  String POLYGON((x y, x y, ...))
     */
    public String pointCoordinatesToOGCPolygon(final double[][] pointCoordinates, final boolean closePolygon) {
        final StringBuffer buffer = new StringBuffer();

        buffer.append("SRID=").append(CismapBroker.getInstance().getDefaultCrsAlias()).append(";");

        if (pointCoordinates.length > 2) {
            buffer.append("POLYGON").append('(').append('('); // NOI18N
        } else if (pointCoordinates.length == 2) {
            buffer.append("LINESTRING").append('(');          // NOI18N
        } else if (pointCoordinates.length == 1) {
            buffer.append("POINT").append('(');               // NOI18N
        }

        if ((pointCoordinates != null) || (pointCoordinates.length != 0)) {
            for (int i = 0; i < pointCoordinates.length; i++) {
                buffer.append(pointCoordinates[i][0]);
                buffer.append(' ');
                buffer.append(pointCoordinates[i][1]);

                if (!closePolygon) {
                    buffer.append((i < (pointCoordinates.length - 1)) ? "," : ""); // NOI18N
                } else {
                    buffer.append(',');
                }
            }

            // polygon schliessen ...
            if (closePolygon) {
                buffer.append(pointCoordinates[0][0]);
                buffer.append(' ');
                buffer.append(pointCoordinates[0][1]);
            }
        }

        buffer.append(')');

        if (pointCoordinates.length > 2) { // polygon
            buffer.append(')');
        }

        return buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   pointCoordinateString  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String pointCoordinatesStringToOGCPolygon(final String pointCoordinateString) {
        return this.pointCoordinatesToOGCPolygon(this.parsePointCoordinatesString(pointCoordinateString));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   boundingBoxCoordinates  DOCUMENT ME!
     * @param   closePolygon            DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String boundingBoxCoordinatesToOGCPolygon(final double[] boundingBoxCoordinates,
            final boolean closePolygon) {
        final double[][] pointCoordinates = new double[2][2];
        pointCoordinates[0][0] = boundingBoxCoordinates[0];
        pointCoordinates[0][1] = boundingBoxCoordinates[1];
        pointCoordinates[1][0] = boundingBoxCoordinates[2];
        pointCoordinates[1][1] = boundingBoxCoordinates[3];

        return this.pointCoordinatesToOGCPolygon(pointCoordinates, closePolygon);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   boundingBoxCoordinates  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String boundingBoxCoordinatesToOGCPolygon(final double[] boundingBoxCoordinates) {
        return this.boundingBoxCoordinatesToOGCPolygon(boundingBoxCoordinates);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   coordinatesString  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String boundingBoxCoordinatesStringToOGCPolygon(final String coordinatesString) {
        return this.boundingBoxCoordinatesToOGCPolygon(this.parseBoundingBoxCoordinatesString(coordinatesString));
    }

    /**
     * Computes the center point of a bounding box.
     *
     * @param   boundingBoxCoordinates  a bounding box (minX, minY, maxX, maxY)
     *
     * @return  point (x/y)
     */
    public double[] getCenterCoordinates(final double[] boundingBoxCoordinates) {
        final double[] centerCoordinate = new double[2];

        // maxX - minX / 2
        centerCoordinate[0] = (boundingBoxCoordinates[2] + boundingBoxCoordinates[0]) / 2;

        // maxY - minY / 2
        centerCoordinate[1] = (boundingBoxCoordinates[3] + boundingBoxCoordinates[1]) / 2;

        return centerCoordinate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   v  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String collectionToSQLString(final Collection v) {
        final StringBuffer buf = new StringBuffer();
        buf.append('(');

        final Iterator i = v.iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            final Object o = i.next();
            buf.append('\'').append(String.valueOf(o)).append('\'');
            hasNext = i.hasNext();
            if (hasNext) {
                buf.append(',');
            }
        }

        buf.append(')');
        return buf.toString();
    }
    // -------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final double[][] pointCoordintes = new double[][] {
                { 0, 1 },
                { 1, 2 },
                { 3, 4 },
                { 5, 6 },
                { 7, 8 },
                { 9, 10 },
                { 11, 12 }
            };
        // System.out.println(NavigatorToolkit.getToolkit().toOGCFeatureString("POLYGON", pointCoordintes));
    }
}
