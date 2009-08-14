/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.custom.objecteditors;

import de.cismet.cids.tools.metaobjectrenderer.Titled;
import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.CoolEditor;
import de.cismet.tools.gui.FooterComponentProvider;
import de.cismet.tools.gui.TitleComponentProvider;
import de.cismet.tools.gui.WrappedComponent;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author thorsten
 */
public class EditorWrapper implements ComponentWrapper {

    public WrappedComponent wrapComponent(JComponent component) {
        System.out.println(component);
        component.setBorder(new EmptyBorder(10, 10, 10, 10));
        CoolEditor ced = new CoolEditor();
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
            ced.getPanTitleAndIcon().add(((FooterComponentProvider) component).getFooterComponent());
        }
        return ced;

    }
}
