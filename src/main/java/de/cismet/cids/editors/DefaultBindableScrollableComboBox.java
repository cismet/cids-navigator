/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Copyright (c) 2001-2009 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.cismet.cids.editors;

import Sirius.server.middleware.types.MetaClass;

import com.jgoodies.looks.plastic.PlasticComboBoxUI;

import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.plaf.ComboBoxUI;

import de.cismet.cids.dynamics.CidsBean;

/**
 * Wraps a DefaultBindableCombobox and add a horizontal scrollbar, if required.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DefaultBindableScrollableComboBox extends DefaultBindableReferenceCombo {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ScrollableComboBox object.
     */
    public DefaultBindableScrollableComboBox() {
        super();
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    /**
     * Creates a new ScrollableComboBox object.
     *
     * @param  mc  DOCUMENT ME!
     */
    public DefaultBindableScrollableComboBox(final MetaClass mc) {
        super(mc);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    /**
     * Creates a new ScrollableComboBox object.
     *
     * @param  comparator  DOCUMENT ME!
     */
    public DefaultBindableScrollableComboBox(final Comparator<CidsBean> comparator) {
        super(comparator);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    /**
     * Creates a new ScrollableComboBox object.
     *
     * @param  mc        DOCUMENT ME!
     * @param  nullable  DOCUMENT ME!
     * @param  onlyUsed  DOCUMENT ME!
     */
    public DefaultBindableScrollableComboBox(final MetaClass mc, final boolean nullable, final boolean onlyUsed) {
        super(mc, nullable, onlyUsed);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    /**
     * Creates a new ScrollableComboBox object.
     *
     * @param  mc          DOCUMENT ME!
     * @param  nullable    DOCUMENT ME!
     * @param  onlyUsed    DOCUMENT ME!
     * @param  comparator  DOCUMENT ME!
     */
    public DefaultBindableScrollableComboBox(final MetaClass mc,
            final boolean nullable,
            final boolean onlyUsed,
            final Comparator<CidsBean> comparator) {
        super(mc, nullable, onlyUsed, comparator);
        if (getUI() instanceof PlasticComboBoxUI) {
            setUI(ScrollableComboUI.createUI(null));
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected ComboBoxUI createRendererUI() {
        return new ScrollableComboUI() {

                @Override
                protected JButton createArrowButton() {
                    return null;
                }
            };
    }
}
