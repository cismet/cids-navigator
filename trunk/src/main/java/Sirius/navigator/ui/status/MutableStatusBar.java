/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.status;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Filename             :
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       16.02.2000
 * History                      :
 *
 *******************************************************************************/
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.widget.MutableImageLabel;

import org.apache.log4j.Logger;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MutableStatusBar extends JPanel {

    //~ Static fields/initializers ---------------------------------------------

    protected static final Logger logger = Logger.getLogger(MutableStatusBar.class);

    private static final ResourceManager resource = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private JLabel status_1;
    private JLabel status_2;
    private JLabel status_3;

    private MutableImageLabel greenStatusIcon;
    private MutableImageLabel redStatusIcon;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MutableStatusBar object.
     */
    public MutableStatusBar() {
        super();
        status_1 = new JLabel(""); // NOI18N
        status_2 = new JLabel(""); // NOI18N
        status_3 = new JLabel(""); // NOI18N
        this.init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * public MutableStatusBar(String s1, String s2, String s3) { super(); status_1 = new JLabel(s1); status_2 = new
     * JLabel(s2); status_3 = new JLabel(s3); this.init(); }.
     */
    protected void init() {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(3, 2, 1, 1));
        final GridBagConstraints constraints = new GridBagConstraints();

        // status_1.setHorizontalAlignment(JLabel.CENTER);
        status_1.setBorder(new CompoundBorder(
                new SoftBevelBorder(SoftBevelBorder.LOWERED),
                new EmptyBorder(0, 2, 0, 2)));
        status_1.setPreferredSize(new Dimension(180, 16));

        status_2.setBorder(new CompoundBorder(
                new SoftBevelBorder(SoftBevelBorder.LOWERED),
                new EmptyBorder(0, 2, 0, 2)));
        status_2.setPreferredSize(new Dimension(200, 16));

        status_3.setBorder(new CompoundBorder(
                new SoftBevelBorder(SoftBevelBorder.LOWERED),
                new EmptyBorder(0, 2, 0, 2)));
        status_3.setPreferredSize(new Dimension(300, 16));

        greenStatusIcon = new MutableImageLabel(
                resource.getIcon("green_off.gif"),
                resource.getIcon("green_on.gif"));
        greenStatusIcon.setBorder(new EmptyBorder(2, 2, 2, 1));

        redStatusIcon = new MutableImageLabel(
                resource.getIcon("red_off.gif"),
                resource.getIcon("red_on.gif"));
        redStatusIcon.setBorder(new EmptyBorder(2, 1, 2, 3));
        // =====================================================================

        constraints.insets = new Insets(0, 0, 0, 4);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.VERTICAL;

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.add(status_1, constraints);

        constraints.weightx = 0.0;
        constraints.gridx++;
        this.add(status_2, constraints);

        constraints.gridx = 2;
        this.add(status_3, constraints);

        constraints.gridx++;
        if (greenStatusIcon != null) {
            this.add(greenStatusIcon, constraints);
        }

        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx++;
        if (redStatusIcon != null) {
            this.add(redStatusIcon, constraints);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  statusMessage    DOCUMENT ME!
     * @param  messagePosition  DOCUMENT ME!
     */
    public void setStatusMessage(final String statusMessage, final int messagePosition) {
        // if(logger.isDebugEnabled())logger.debug("setStatusMessage: '" + statusMessage + "' @position: '" +
        // messagePosition + "'");
        switch (messagePosition) {
            case Status.MESSAGE_IGNORE: {
                break;
            }
            case Status.MESSAGE_POSITION_1: {
                status_1.setText(statusMessage);
                break;
            }
            case Status.MESSAGE_POSITION_2: {
                status_2.setText(statusMessage);
                break;
            }
            case Status.MESSAGE_POSITION_3: {
                status_3.setText(statusMessage);
                break;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  greenIconStatus  DOCUMENT ME!
     */
    public void setGreenIconStatus(final int greenIconStatus) {
        // if(logger.isDebugEnabled())logger.debug("setGreenIconStatus: '" + greenIconStatus + "'");
        switch (greenIconStatus) {
            case Status.ICON_IGNORE: {
                break;
            }
            case Status.ICON_ACTIVATED: {
                greenStatusIcon.switchOn(true);
                break;
            }
            case Status.ICON_DEACTIVATED: {
                greenStatusIcon.switchOff(true);
                break;
            }
            case Status.ICON_BLINKING: {
                greenStatusIcon.blink(500);
                break;
            }
            default: {
                greenStatusIcon.switchOff(true);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  redIconStatus  DOCUMENT ME!
     */
    public void setRedIconStatus(final int redIconStatus) {
        // if(logger.isDebugEnabled())logger.debug("setRedIconStatus: '" + redIconStatus + "'");
        switch (redIconStatus) {
            case Status.ICON_IGNORE: {
                break;
            }
            case Status.ICON_ACTIVATED: {
                redStatusIcon.switchOn(true);
                break;
            }
            case Status.ICON_DEACTIVATED: {
                redStatusIcon.switchOff(true);
                break;
            }
            case Status.ICON_BLINKING: {
                redStatusIcon.blink(500);
                break;
            }
            default: {
                redStatusIcon.switchOn(true);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  status  DOCUMENT ME!
     */
    public void setStatus(final Status status) {
        this.setStatusMessage(status.getStatusMessage(), status.getMessagePosition());
        this.setRedIconStatus(status.getRedIconState());
        this.setGreenIconStatus(status.getGreenIconState());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isGreenStatusIconBlinking() {
        return greenStatusIcon.isBlinking();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isRedStatusIconBlinking() {
        return redStatusIcon.isBlinking();
    }
}
