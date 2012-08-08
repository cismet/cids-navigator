/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultMetaObjectRenderer.java
 *
 * Created on 9. Mai 2007, 15:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.MetaObject;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import de.cismet.tools.gui.TitleComponentProvider;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class DefaultMetaObjectRenderer extends MetaObjectRenderer implements TitleComponentProvider {

    //~ Instance fields --------------------------------------------------------

    MetaObject mo;
    JPanel comp = null;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private JLabel titleComponent;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DefaultMetaObjectRenderer.
     */
    public DefaultMetaObjectRenderer() {
        super();
        titleComponent = new JLabel();
        titleComponent.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        titleComponent.setForeground(new java.awt.Color(255, 255, 255));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public double getWidthRatio() {
        return 0.75;
    }

    @Override
    public JComponent getAggregationRenderer(final Collection<MetaObject> cm, final String title) {
        return null;
    }

    @Override
    public JComponent getSingleRenderer(final MetaObject mo, final String title) {
        this.mo = mo;
        titleComponent.setText(title);
        try {
            int y = 0;
            final GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);
            java.awt.GridBagConstraints gridBagConstraints;

            // FILLING STUFF
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 10;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            final JPanel filler = new JPanel();
            filler.setOpaque(false);
            add(filler, gridBagConstraints);

            final int columns = 2;

            final HashMap attributes = mo.getAttributes();
            final Iterator keys = attributes.keySet().iterator();

            while (keys.hasNext()) {
                final Object key = keys.next();
                final Attribute attr = (Attribute)attributes.get(key);
                if ((attr != null) && attr.isVisible()) {
                    // ARRAY
                    if ((attr.getValue() instanceof MetaObject) && attr.isArray()) {
                        // Sammeln der richtigen MetaObjects in einem Vector<MetaObject>
                        String attrName = ""; // NOI18N
                        final Vector<MetaObject> arrayObjects = new Vector<MetaObject>();
                        final MetaObject artificialObject = (MetaObject)attr.getValue();
                        final HashMap artificialAttributes = artificialObject.getAttributes();
                        final Iterator artificialKeySetIterator = artificialAttributes.keySet().iterator();
                        while (artificialKeySetIterator.hasNext()) {
                            final Attribute a = (Attribute)artificialAttributes.get(artificialKeySetIterator.next());
                            final MetaObject referenceMetaObject = (MetaObject)a.getValue();
                            // Es gibt nur ein Attribut in diesem Objekt das ein MetaObject ist
                            final Attribute aa = (Attribute)
                                referenceMetaObject.getAttributesByType(MetaObject.class).toArray()[0];
                            arrayObjects.add((MetaObject)aa.getValue());
                            attrName = aa.getName();
                        }

                        comp = new JPanel();
                        comp.setOpaque(false);
                        comp.setLayout(new GridBagLayout());
                        // FlowLayout flow=new FlowLayout(FlowLayout.LEADING);
                        // comp.setLayout(flow);

                        int yArray = 0;
                        int xArray = 0;
                        final int ySize = 0;
                        JPanel panel = null;

                        for (final MetaObject arrayElement : arrayObjects) {
                            if (xArray == 0) {
                                gridBagConstraints = new java.awt.GridBagConstraints();
                                gridBagConstraints.gridx = 0;
                                gridBagConstraints.gridy = yArray;
                                gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
                                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                                gridBagConstraints.gridwidth = 2;
                                panel = new JPanel();
                                panel.setOpaque(false);
                                panel.setLayout(new FlowLayout(FlowLayout.LEFT));
                                comp.add(panel, gridBagConstraints);
                            }
                            final JComponent mor =
                                new DefaultMetaObjectRenderer().getSingleRenderer(arrayElement, attrName);
                            panel.add(mor);
                            // ySize+=mor.getPreferredSize().getHeight();
                            // comp.add(new DefaultMetaObjectRenderer(arrayElement,attrName),gridBagConstraints);
                            // comp.add(mor);
                            if (xArray < (columns - 1)) {
                                xArray++;
                            } else {
                                xArray = 0;
                                yArray++;
                            }
                        }
                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = y++;
                        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 2);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                        gridBagConstraints.gridwidth = 2;
                        // gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                        add(comp, gridBagConstraints);
                        // comp.setPreferredSize(new Dimension(800,ySize));
                    } else {
                        final JLabel lblDesc = new JLabel(attr.getName());
                        lblDesc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                        String val = " ";                                    // NOI18N
                        if (attr.getValue() != null) {
                            val = attr.getValue().toString();
                        }

                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = y;
                        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        add(lblDesc, gridBagConstraints);

                        gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 1;
                        gridBagConstraints.gridy = y;
                        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 2);
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        final JComponent comp = MetaAttributeRendererFactory.getInstance().getRenderer(attr);
                        add(comp, gridBagConstraints);
                        y++;
                    }
                }
            }

            final Border b = new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
                    javax.swing.BorderFactory.createTitledBorder(title));
            this.setBorder(b);
        } catch (Throwable t) {
            log.error("Error while creating the MetaObjectRenderer", t); // NOI18N
        }
        setOpaque(false);
        return this;
    }

    @Override
    public JComponent getTitleComponent() {
        return titleComponent;
    }
}
