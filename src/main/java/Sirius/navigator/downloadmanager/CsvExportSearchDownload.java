/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.downloadmanager;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import java.io.PrintWriter;

import java.util.Collection;
import java.util.List;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ClientConnectionContextProvider;
import de.cismet.cids.server.search.builtin.CsvExportSearchStatement;

import de.cismet.tools.gui.downloadmanager.AbstractDownload;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CsvExportSearchDownload extends AbstractDownload implements ClientConnectionContextProvider {

    //~ Instance fields --------------------------------------------------------

    private final CsvExportSearchStatement search;
    private final List<String> header;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CsvExportSearchDownload object.
     *
     * @param  search     DOCUMENT ME!
     * @param  title      DOCUMENT ME!
     * @param  directory  DOCUMENT ME!
     * @param  filename   DOCUMENT ME!
     * @param  header     DOCUMENT ME!
     */
    public CsvExportSearchDownload(final CsvExportSearchStatement search,
            final String title,
            final String directory,
            final String filename,
            final List<String> header) {
        this.search = search;
        this.title = title;
        this.directory = directory;
        this.header = header;

        status = State.WAITING;

        determineDestinationFile(filename, ".csv");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void run() {
        if (status != State.WAITING) {
            return;
        }

        status = State.RUNNING;
        stateChanged();

        final Collection<String> csvColl;
        try {
            csvColl = (Collection)SessionManager.getProxy()
                        .customServerSearch(SessionManager.getSession().getUser(),
                                search,
                                getClientConnectionContext());
        } catch (final ConnectionException ex) {
            error(ex);
            return;
        }

        if ((csvColl == null) || (csvColl.isEmpty())) {
            log.info("Downloaded content seems to be empty..");

            if (status == State.RUNNING) {
                status = State.COMPLETED;
                stateChanged();
            }

            return;
        }

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileToSaveTo);

            if (header != null) {
                final String headerCsv = CsvExportSearchStatement.implode(header.toArray(new String[0]),
                        CsvExportSearchStatement.CSV_SEPARATOR);
                writer.println(headerCsv);
            }
            for (final String csvLine : csvColl) {
                writer.println(csvLine);
            }
        } catch (final Exception ex) {
            log.warn("Couldn't write downloaded content to file '" + fileToSaveTo + "'.", ex);
            error(ex);
            return;
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                }
            }
        }

        if (status == State.RUNNING) {
            status = State.COMPLETED;
            stateChanged();
        }
    }

    @Override
    public ClientConnectionContext getClientConnectionContext() {
        return ClientConnectionContext.create(getClass().getSimpleName());
    }
}
