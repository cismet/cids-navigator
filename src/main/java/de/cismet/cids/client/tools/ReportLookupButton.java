/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

import org.openide.util.Lookup;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.utils.interfaces.ReportAction;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class ReportLookupButton extends JButton {

    //~ Static fields/initializers ---------------------------------------------

    static Collection<? extends ReportAction> allReportActions = Lookup.getDefault().lookupAll(ReportAction.class);

    //~ Instance fields --------------------------------------------------------

    private ReportAction reportAction = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReportLookupButton object.
     *
     * @param   reportKey  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public ReportLookupButton(final String reportKey) {
        assert (reportKey != null);
        for (final ReportAction ra : allReportActions) {
            if (ra.getReportKey().equalsIgnoreCase(reportKey)) {
                setAction(ra);
                reportAction = ra;
                break;
            }
        }
        if (reportAction == null) {
            throw new IllegalArgumentException("Could not find report with this reportKey:" + reportKey
                        + " Check your Classpath and the assigned reportKeys.");
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  beans  DOCUMENT ME!
     */
    public void setBeans(final Collection<CidsBean> beans) {
        assert ((beans != null) && (beans.size() > 0));
        reportAction.setCidsBeans(beans);
    }
    /**
     * DOCUMENT ME!
     *
     * @param  bean  DOCUMENT ME!
     */
    public void setBean(final CidsBean bean) {
        assert ((bean != null) && (reportAction != null));
        final ArrayList<CidsBean> al = new ArrayList<CidsBean>(1);
        al.add(bean);
        reportAction.setCidsBeans(al);
    }
}
