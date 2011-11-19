/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

import org.openide.util.Lookup;

import java.awt.event.ActionEvent;

import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractAction;
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

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    private ReportAction reportAction = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReportLookupButton object.
     *
     * @param  reportKey  DOCUMENT ME!
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
            reportAction = new DummyAction();
            log.warn("Could not find report with this reportKey:" + reportKey
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

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class DummyAction extends AbstractAction implements ReportAction {

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public String getReportKey() {
        return "";
    }

    @Override
    public Collection<CidsBean> getCidsBeans() {
        return null;
    }

    @Override
    public void setCidsBeans(final Collection<CidsBean> beans) {
    }
}
