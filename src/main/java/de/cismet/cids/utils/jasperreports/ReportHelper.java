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

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        final List<PdfReader> pdfReaders = new ArrayList<PdfReader>();

        try {
            // create the pdf reader and determine the total number of pages
            for (final InputStream pdf : streamOfPDFFiles) {
                final PdfReader pdfReader = new PdfReader(pdf);
                pdfReaders.add(pdfReader);
                totalNumOfPages += pdfReader.getNumberOfPages();
            }

            final BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            final PdfCopyFields copy = new PdfCopyFields(outputStream);
            copy.open();

            int currentPageNumber = 0;
            for (final PdfReader pdfReader : pdfReaders) {
                final ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
                final PdfStamper stamper = new PdfStamper(pdfReader, pdfOut);
                int currentNumOfPages = 0;
                while (currentNumOfPages < pdfReader.getNumberOfPages()) {
                    currentNumOfPages++;
                    currentPageNumber++;

                    final PdfContentByte canvas = stamper.getOverContent(currentNumOfPages);

                    if (paginate) {
                        // add the page number
                        canvas.beginText();
                        canvas.setFontAndSize(baseFont, 9);
                        canvas.showTextAligned(
                            PdfContentByte.ALIGN_CENTER,
                            currentPageNumber
                                    + NbBundle.getMessage(
                                        ReportHelper.class,
                                        "ReportHelper.concatPDFs.pageNumberSeparator")
                                    + totalNumOfPages,
                            520,
                            5,
                            0);
                        canvas.endText();
                    }
                }
                stamper.close();
                pdfOut.close();
                final PdfReader modifiedPdfReader = new PdfReader(new ByteArrayInputStream(pdfOut.toByteArray()));
                String pageNOs = "";
                final int noOfPages = modifiedPdfReader.getNumberOfPages();

                if (noOfPages > 0) {
                    pageNOs = getNumderOfPages(noOfPages);
                }
                copy.addDocument(modifiedPdfReader, pageNOs);

                modifiedPdfReader.close();
                pdfReader.close();
            }
            copy.close();
        } catch (Exception ex) {
            LOG.error("error while merging pdfs", ex);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                LOG.error("error while closing pdfstream", ex);
            }
        }
    }

    /**
     * Get comma separated page numbers as string.
     *
     * @param   noOfPages  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String getNumderOfPages(final int noOfPages) {
        String pageNOs = "";

        for (int i = 0; i < noOfPages; i++) {
            final Integer page = i;
            if (pageNOs.equals("")) {
                pageNOs = page.toString();
            } else {
                pageNOs = pageNOs.concat("," + page.toString());
            }
        }

        return pageNOs;
    }
}
