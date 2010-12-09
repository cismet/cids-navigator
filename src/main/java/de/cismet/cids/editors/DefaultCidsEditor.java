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
package de.cismet.cids.editors;

import org.jdesktop.beansbinding.BindingGroup;

import java.awt.LayoutManager;

import java.util.HashMap;

import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.tools.metaobjectrenderer.Titled;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultCidsEditor extends JPanel implements AutoBindableCidsEditor, Titled {

    //~ Instance fields --------------------------------------------------------

    private HashMap<String, Bindable> controls = new HashMap<String, Bindable>();
    private CidsBean cidsBean = null;
    private BindingGroup bindingGroup = new BindingGroup();
    private String customTitle = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultCidsEditor object.
     */
    public DefaultCidsEditor() {
        init();
    }

    /**
     * Creates a new DefaultCidsEditor object.
     *
     * @param  isDoubleBuffered  DOCUMENT ME!
     */
    public DefaultCidsEditor(final boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    /**
     * Creates a new DefaultCidsEditor object.
     *
     * @param  layout  DOCUMENT ME!
     */
    public DefaultCidsEditor(final LayoutManager layout) {
        super(layout);
        init();
    }

    /**
     * Creates a new DefaultCidsEditor object.
     *
     * @param  layout            DOCUMENT ME!
     * @param  isDoubleBuffered  DOCUMENT ME!
     */
    public DefaultCidsEditor(final LayoutManager layout, final boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        setOpaque(false);
    }

    @Override
    public void addControlInformation(final String name, final Bindable component) {
        controls.put(name, component);
    }

    @Override
    public Bindable getControlByName(final String name) {
        return controls.get(name);
    }

    @Override
    public HashMap<String, Bindable> getAllControls() {
        return controls;
    }

    @Override
    public BindingGroup getBindingGroup() {
        return bindingGroup;
    }

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        this.cidsBean = cidsBean;
    }

    @Override
    public String getTitle() {
        if (customTitle != null) {
            return customTitle;
        } else if ((cidsBean != null) && (cidsBean.getMetaObject() != null)) {
            return cidsBean.getMetaObject().getMetaClass().getName();
        } else {
            return org.openide.util.NbBundle.getMessage(
                    DefaultCidsEditor.class,
                    "DefaultCidsEditor.getTitle().defaultTitle"); // NOI18N
        }
    }

    @Override
    public void setTitle(final String title) {
        customTitle = title;
    }

    @Override
    public void dispose() {
        bindingGroup.unbind();
    }
}
