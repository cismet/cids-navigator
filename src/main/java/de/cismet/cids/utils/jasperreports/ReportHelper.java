/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.utils.jasperreports;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class ReportHelper {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = org.apache.log4j.Logger.getLogger(ReportHelper.class);

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  streamOfPDFFiles  DOCUMENT ME!
     * @param  outputStream      DOCUMENT ME!
     * @param  paginate          DOCUMENT ME!
     */
    public static void concatPDFs(final List<InputStream> streamOfPDFFiles,
            final OutputStream outputStream,
            final boolean paginate) {
        int totalNumOfPages = 0;
        final Document document = new Document();
        final List<InputStream> inputStreams = streamOfPDFFiles;
        final List<PdfReader> pdfReaders = new ArrayList<PdfReader>();

        try {
            final PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            for (final InputStream pdf : inputStreams) {
                final PdfReader pdfReader = new PdfReader(pdf);
                pdfReaders.add(pdfReader);
                totalNumOfPages += pdfReader.getNumberOfPages();
            }

            document.open();
            final BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            final PdfContentByte contentByte = writer.getDirectContent();

            PdfImportedPage page;
            int currentPageNumber = 0;

            for (final PdfReader pdfReader : pdfReaders) {
                int currentNumOfPages = 0;
                while (currentNumOfPages < pdfReader.getNumberOfPages()) {
                    currentNumOfPages++;
                    currentPageNumber++;

                    document.setPageSize(pdfReader.getPageSizeWithRotation(currentNumOfPages));
                    document.newPage();

                    page = writer.getImportedPage(pdfReader, currentNumOfPages);
                    contentByte.addTemplate(page, 0, 0);

                    if (paginate) {
                        contentByte.beginText();
                        contentByte.setFontAndSize(baseFont, 9);
                        contentByte.showTextAligned(
                            PdfContentByte.ALIGN_CENTER,
                            currentPageNumber
                                    + " of "
                                    + totalNumOfPages,
                            520,
                            5,
                            0);
                        contentByte.endText();
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("error while merging pdfs", ex);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                LOG.error("error whil closing pdfstream", ex);
            }
        }
    }
}
