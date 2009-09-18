/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.custom.objecteditors;

import de.cismet.cids.tools.metaobjectrenderer.Titled;
import de.cismet.tools.gui.BorderProvider;
import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.CoolEditor;
import de.cismet.tools.gui.FooterComponentProvider;
import de.cismet.tools.gui.TitleComponentProvider;
import de.cismet.tools.gui.WrappedComponent;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;

/**
 *
 * @author thorsten
 */
public class EditorWrapper implements ComponentWrapper {

    private static final Logger log = Logger.getLogger(EditorWrapper.class);

    public WrappedComponent wrapComponent(JComponent component) {
        component.setBorder(new EmptyBorder(10, 10, 10, 10));
        final CoolEditor ced = new CoolEditor();
        if (component instanceof BorderProvider) {
            final BorderProvider borderProvider = (BorderProvider) component;
            ced.getPanEdit().setBorder(borderProvider.getCenterrBorder());
            ced.getPanFooter().setBorder(borderProvider.getFooterBorder());
            ced.getPanTitleAndIcon().setBorder(borderProvider.getTitleBorder());
        }
        ced.setOriginalComponent(component);
        component.setOpaque(false);
        ced.getPanEdit().add(component, BorderLayout.CENTER);
        if (component instanceof TitleComponentProvider) {
            ced.getPanTitleAndIcon().add(((TitleComponentProvider) component).getTitleComponent());
        } else if (component instanceof Titled) {
            JLabel l = new JLabel(((Titled) component).getTitle());
            l.setFont(new java.awt.Font("Tahoma", 1, 18));
            l.setForeground(new java.awt.Color(255, 255, 255));
            ced.getPanTitleAndIcon().add(l);
        }

        if (component instanceof FooterComponentProvider) {
            ced.getPanFooter().add(((FooterComponentProvider) component).getFooterComponent());
        }
        return ced;

    }
}
