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
/*
 * DefaultBindableTimestampChooser.java
 *
 * Created on 01.04.2009, 16:36:50
 */
package de.cismet.cids.editors;

import net.sf.jasperreports.engine.design.events.PropagationChangeListener;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.cismet.cids.editors.converters.SqlTimestampToUtilDateConverter;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBindableTimestampChooser extends javax.swing.JPanel implements Bindable {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_TIMESTAMP = "timestamp";     // NOI18N
    public static final String CARDS_CHOOSE = "chooseTimestamp"; // NOI18N
    public static final String CARDS_CREATE = "createTimestamp"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    Calendar mainC = Calendar.getInstance();

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
//    private Date timestamp;
    private Date date;
    private Date time;
    private PropertyChangeSupport propertyChangeSupport;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateTimestamp;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JPanel panChooseTimestamp;
    private javax.swing.JPanel panCreateTimestamp;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DefaultBindableTimestampChooser.
     */
    public DefaultBindableTimestampChooser() {
        mainC.setTimeInMillis(0);
        initComponents();
        jXDatePicker1.getMonthView().setSelectionModel(new SingleDaySelectionModel());
        setTimestamp(null);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    /**
     * Get the value of timestamp.
     *
     * @return  the value of timestamp
     */
    public Date getTimestamp() {
        try {
            final Calendar dateC = Calendar.getInstance();
            dateC.setTimeInMillis(0);
            if (date != null) {
                dateC.setTime(date);
            }
            final int day = dateC.get(Calendar.DAY_OF_MONTH);
            final int month = dateC.get(Calendar.MONTH);
            final int year = dateC.get(Calendar.YEAR);
            final Calendar timeC = Calendar.getInstance();
            timeC.setTimeInMillis(0);
            if (time != null) {
                timeC.setTime(time);
            }
            final int hour = timeC.get(Calendar.HOUR_OF_DAY);
            final int minute = timeC.get(Calendar.MINUTE);

            mainC.set(year, month, day, hour, minute);
            return mainC.getTime();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Error while fetching timestamp" + e); // NOI18N
            }
            return null;
        }
    }

    /**
     * Set the value of timestamp.
     *
     * @param  timestamp  new value of timestamp
     */
    public void setTimestamp(final Date timestamp) {
        if (log.isDebugEnabled()) {
            log.debug("setTimestamp: " + timestamp); // NOI18N
        }
        if (timestamp == null) {
            ((CardLayout)getLayout()).show(this, CARDS_CREATE);
        } else {
            ((CardLayout)getLayout()).show(this, CARDS_CHOOSE);
        }

        try {
            final Date oldTimestamp = mainC.getTime();
            if (timestamp != null) {
                mainC.setTime(timestamp);
            }

            final int day = mainC.get(Calendar.DAY_OF_MONTH);
            final int month = mainC.get(Calendar.MONTH);
            final int year = mainC.get(Calendar.YEAR);

            final Calendar dateCal = Calendar.getInstance();
            dateCal.setTimeInMillis(0);
            dateCal.set(year, month, day);

            final int hour = mainC.get(Calendar.HOUR_OF_DAY);
            final int minute = mainC.get(Calendar.MINUTE);

            final Calendar timeCal = Calendar.getInstance();
            timeCal.setTimeInMillis(0);
            timeCal.set(0, 0, 0, hour, minute);

            date = dateCal.getTime();
            time = timeCal.getTime();

            getPropertyChangeSupport().firePropertyChange(PROP_TIMESTAMP, null, getTimestamp());
            bindingGroup.unbind();
            bindingGroup.bind();
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("setTimestamp failed", t); // NOI18N
            }
        }
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (listener != null) {
            getPropertyChangeSupport().addPropertyChangeListener(listener);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        panCreateTimestamp = new javax.swing.JPanel();
        btnCreateTimestamp = new javax.swing.JButton();
        panChooseTimestamp = new javax.swing.JPanel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();

        setOpaque(false);
        setLayout(new java.awt.CardLayout());

        panCreateTimestamp.setLayout(new java.awt.BorderLayout());

        btnCreateTimestamp.setText(org.openide.util.NbBundle.getMessage(
                DefaultBindableTimestampChooser.class,
                "DefaultBindableTimestampChooser.btnCreateTimestamp.text"));        // NOI18N
        btnCreateTimestamp.setToolTipText(org.openide.util.NbBundle.getMessage(
                DefaultBindableTimestampChooser.class,
                "DefaultBindableTimestampChooser.btnCreateTimestamp.toolTipText")); // NOI18N
        btnCreateTimestamp.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCreateTimestampActionPerformed(evt);
                }
            });
        panCreateTimestamp.add(btnCreateTimestamp, java.awt.BorderLayout.CENTER);

        add(panCreateTimestamp, "createTimestamp");

        panChooseTimestamp.setOpaque(false);
        panChooseTimestamp.setLayout(new java.awt.GridBagLayout());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${date}"),
                jXDatePicker1,
                org.jdesktop.beansbinding.BeanProperty.create("date"));
        bindingGroup.addBinding(binding);

        binding.setValidator(new DateValidator());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panChooseTimestamp.add(jXDatePicker1, gridBagConstraints);

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT))));
        jFormattedTextField1.setMinimumSize(new java.awt.Dimension(80, 28));
        jFormattedTextField1.setPreferredSize(new java.awt.Dimension(80, 28));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                this,
                org.jdesktop.beansbinding.ELProperty.create("${time}"),
                jFormattedTextField1,
                org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        binding.setValidator(new TimeValidator());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panChooseTimestamp.add(jFormattedTextField1, gridBagConstraints);

        add(panChooseTimestamp, "chooseTimestamp");

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCreateTimestampActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCreateTimestampActionPerformed
        setTimestamp(Calendar.getInstance().getTime());
    }                                                                                      //GEN-LAST:event_btnCreateTimestampActionPerformed

    @Override
    public String getBindingProperty() {
        return "timestamp"; // NOI18N
    }

    @Override
    public Converter getConverter() {
        return new SqlTimestampToUtilDateConverter();
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Date getDate() {
        return date;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  date  DOCUMENT ME!
     */
    public void setDate(final Date date) {
        if (log.isDebugEnabled()) {
            log.debug("setDate: " + date); // NOI18N
        }
        this.date = date;
        getPropertyChangeSupport().firePropertyChange(PROP_TIMESTAMP, null, getTimestamp());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Date getTime() {
        return time;
    }

    @Override
    public Object getNullSourceValue() {
        return null;
    }

    @Override
    public Object getErrorSourceValue() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  time  DOCUMENT ME!
     */
    public void setTime(final Date time) {
        if (log.isDebugEnabled()) {
            log.debug("setTime: " + time); // NOI18N
        }
        this.time = time;
        getPropertyChangeSupport().firePropertyChange(PROP_TIMESTAMP, null, getTimestamp());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final JFrame jf = new JFrame();
                    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    jf.getContentPane().setLayout(new BorderLayout());
                    jf.setSize(400, 200);
                    final DefaultBindableTimestampChooser dc = new DefaultBindableTimestampChooser();
                    // dc.setTimestamp(Calendar.getInstance().getTime());
                    final JButton cmd = new JButton(
                            org.openide.util.NbBundle.getMessage(
                                DefaultBindableTimestampChooser.class,
                                "DefaultBindableTimestampChooser.main(String).Runnable.run().cmd")); // NOI18N
                    cmd.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                dc.setTimestamp(null);
                            }
                        });
                    jf.getContentPane().add(dc, BorderLayout.NORTH);
                    jf.getContentPane().add(cmd, BorderLayout.SOUTH);
                    jf.setVisible(true);
                }
            });
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class DateValidator extends Validator<Date> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Result validate(final Date value) {
            if (log.isDebugEnabled()) {
                log.debug("DateValidator validate: " + value); // NOI18N
            }
            if (value == null) {
                jXDatePicker1.setDate(date);
                return new Result(null, "Date is null");       // NOI18N
            }
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class TimeValidator extends Validator<Date> {

        //~ Methods ------------------------------------------------------------

        @Override
        public Result validate(final Date value) {
            if (log.isDebugEnabled()) {
                log.debug("TimeValidator validate: " + value); // NOI18N
            }
            if (value == null) {
                return new Result(null, "Time is null");       // NOI18N
            }
            return null;
        }
    }
}
