/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.docking;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import net.infonode.docking.View;

/**
 *
 * @author spuhl
 */
public class CustomView extends View {

    private String id;
    private String viewName;
    private Icon viewIcon;
    private JMenuItem item;

    public CustomView(String id, String viewName, Icon viewIcon, Component comp) {
        super(viewName, viewIcon, comp);
        this.id = id;
        this.viewName = viewName;
        this.viewIcon = viewIcon;
        item = new JMenuItem(viewName, viewIcon);
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (CustomView.this.isClosable()) {
                    CustomView.this.close();
                } else {
                    CustomView.this.restore();
                }
            }
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Icon getViewIcon() {
        return viewIcon;
    }

    public void setViewIcon(Icon viewIcon) {
        this.viewIcon = viewIcon;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public JMenuItem getMenuItem() {
        return item;
    }
    
}
