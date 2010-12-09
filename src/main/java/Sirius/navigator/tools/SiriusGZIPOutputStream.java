/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

/*******************************************************************************

Copyright (c)   :       EIG (Environmental Informatics Group)
http://www.htw-saarland.de/eig
Prof. Dr. Reiner Guettler
Prof. Dr. Ralf Denzer

HTWdS
Hochschule fuer Technik und Wirtschaft des Saarlandes
Goebenstr. 40
66117 Saarbruecken
Germany

Programmers             :       Pascal

Project                 :       WuNDA 2
Filename                :
Version                 :       1.0
Purpose                 :
Created                 :       01.10.1999
History                 :

*******************************************************************************/
import java.io.*;

import java.util.zip.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SiriusGZIPOutputStream extends GZIPOutputStream {

    //~ Instance fields --------------------------------------------------------

    protected Deflater deflater = new Deflater();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SiriusGZIPOutputStream object.
     *
     * @param   in  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public SiriusGZIPOutputStream(final OutputStream in) throws IOException {
        super(in);
    }

    /**
     * Creates a new SiriusGZIPOutputStream object.
     *
     * @param   in    DOCUMENT ME!
     * @param   size  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public SiriusGZIPOutputStream(final OutputStream in, final int size) throws IOException {
        super(in, size);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void write(final byte[] b, final int offset, final int length) throws IOException {
        final byte[] buffer = new byte[1024];
        deflater.reset();
        deflater.setInput(b, offset, length);
        deflater.finish();

        if (length < 128) {
            deflater.setLevel(deflater.NO_COMPRESSION);
        } else {
            deflater.setLevel(deflater.DEFAULT_COMPRESSION);
        }

        int deflated;

        while (!deflater.finished()) {
            deflated = deflater.deflate(buffer);
            super.write(buffer, 0, deflated);
        }
    }
}
