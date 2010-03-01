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

import de.cismet.cids.editors.converters.SqlTimestampToUtilDateConverter;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
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
import net.sf.jasperreports.engine.design.events.PropagationChangeListener;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;

/**
 *
 * @author thorsten
 */
public class DefaultBindableTimestampChooser extends javax.swing.JPanel implements Bindable {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
//    private Date timestamp;
    private Date date;
    private Date time;
    Calendar mainC = Calendar.getInstance();
    public static final String PROP_TIMESTAMP = "timestamp";
    public static final String CARDS_CHOOSE = "chooseTimestamp";
    public static final String CARDS_CREATE = "createTimestamp";
    private PropertyChangeSupport propertyChangeSupport;

    /** Creates new form DefaultBindableTimestampChooser */
    public DefaultBindableTimestampChooser() {
        mainC.setTimeInMillis(0);
        initComponents();
        jXDatePicker1.getMonthView().setSelectionModel(new SingleDaySelectionModel());
        setTimestamp(null);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    /**
     * Get the value of timestamp
     *
     * @return the value of timestamp
     */
    public Date getTimestamp() {
        try {
            Calendar dateC = Calendar.getInstance();
            dateC.setTimeInMillis(0);
            if (date != null) {
                dateC.setTime(date);
            }
            int day = dateC.get(Calendar.DAY_OF_MONTH);
            int month = dateC.get(Calendar.MONTH);
            int year = dateC.get(Calendar.YEAR);
            Calendar timeC = Calendar.getInstance();
            timeC.setTimeInMillis(0);
            if (time != null) {
                timeC.setTime(time);
            }
            int hour = timeC.get(Calendar.HOUR_OF_DAY);
            int minute = timeC.get(Calendar.MINUTE);

            mainC.set(year, month, day, hour, minute);
            return mainC.getTime();
        } catch (Exception e) {
            log.debug("Fehler beim Abrufen von Timestamp" + e);
            return null;
        }
    }

    /**
     * Set the value of timestamp
     *
     * @param timestamp new value of timestamp
     */
    public void setTimestamp(Date timestamp) {
        log.debug("setTimestamp: " + timestamp);
        if (timestamp == null) {
            ((CardLayout)getLayout()).show(this, CARDS_CREATE);
        } else {
            ((CardLayout)getLayout()).show(this, CARDS_CHOOSE);
        }

        try {
            Date oldTimestamp = mainC.getTime();
            if (timestamp != null) {
                mainC.setTime(timestamp);
            }


            int day = mainC.get(Calendar.DAY_OF_MONTH);
            int month = mainC.get(Calendar.MONTH);
            int year = mainC.get(Calendar.YEAR);

            Calendar dateCal = Calendar.getInstance();
            dateCal.setTimeInMillis(0);
            dateCal.set(year, month, day);

            int hour = mainC.get(Calendar.HOUR_OF_DAY);
            int minute = mainC.get(Calendar.MINUTE);

            Calendar timeCal = Calendar.getInstance();
            timeCal.setTimeInMillis(0);
            timeCal.set(0, 0, 0, hour, minute);


            date = dateCal.getTime();
            time = timeCal.getTime();


            getPropertyChangeSupport().firePropertyChange(PROP_TIMESTAMP, null, getTimestamp());
            bindingGroup.unbind();
            bindingGroup.bind();
        } catch (Throwable t) {
            log.debug("setTimestamp failed", t);
        }
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            getPropertyChangeSupport().addPropertyChangeListener(listener);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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

        btnCreateTimestamp.setText("kein Datum angelegt");
        btnCreateTimestamp.setToolTipText("Hier klicken um ein Datum auszuwählen.");
        btnCreateTimestamp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateTimestampActionPerformed(evt);
            }
        });
        panCreateTimestamp.add(btnCreateTimestamp, java.awt.BorderLayout.CENTER);

        add(panCreateTimestamp, "createTimestamp");

        panChooseTimestamp.setOpaque(false);
        panChooseTimestamp.setLayout(new java.awt.GridBagLayout());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${date}"), jXDatePicker1, org.jdesktop.beansbinding.BeanProperty.create("date"));
        bindingGroup.addBinding(binding);

        binding.setValidator(new DateValidator());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panChooseTimestamp.add(jXDatePicker1, gridBagConstraints);

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT))));
        jFormattedTextField1.setMinimumSize(new java.awt.Dimension(80, 28));
        jFormattedTextField1.setPreferredSize(new java.awt.Dimension(80, 28));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${time}"), jFormattedTextField1, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        binding.setValidator(new TimeValidator());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panChooseTimestamp.add(jFormattedTextField1, gridBagConstraints);

        add(panChooseTimestamp, "chooseTimestamp");

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreateTimestampActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateTimestampActionPerformed
        setTimestamp(Calendar.getInstance().getTime());
    }//GEN-LAST:event_btnCreateTimestampActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateTimestamp;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JPanel panChooseTimestamp;
    private javax.swing.JPanel panCreateTimestamp;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    public String getBindingProperty() {
        return "timestamp";
    }

    public Converter getConverter() {
        return new SqlTimestampToUtilDateConverter();
    }

    public Validator getValidator() {
        return null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        log.debug("setDate: " + date);
        this.date = date;
        getPropertyChangeSupport().firePropertyChange(PROP_TIMESTAMP, null, getTimestamp());
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        log.debug("setTime: " + time);
        this.time = time;
        getPropertyChangeSupport().firePropertyChange(PROP_TIMESTAMP, null, getTimestamp());
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                JFrame jf = new JFrame();
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.getContentPane().setLayout(new BorderLayout());
                jf.setSize(400, 200);
                final DefaultBindableTimestampChooser dc = new DefaultBindableTimestampChooser();
                //dc.setTimestamp(Calendar.getInstance().getTime());
                JButton cmd = new JButton("TEST (Datum löschen)");
                cmd.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dc.setTimestamp(null);
                    }
                });
                jf.getContentPane().add(dc, BorderLayout.NORTH);
                jf.getContentPane().add(cmd, BorderLayout.SOUTH);
                jf.setVisible(true);
            }
        });
    }

    class DateValidator extends Validator<Date> {

        @Override
        public Result validate(Date value) {
            log.debug("DateValidator validate: " + value);
            if (value == null) {
                jXDatePicker1.setDate(date);
                return new Result(null, "Date is null");
            }
            return null;
        }
    }

    class TimeValidator extends Validator<Date> {

        @Override
        public Result validate(Date value) {
            log.debug("TimeValidator validate: " + value);
            if (value == null) {
                return new Result(null, "Time is null");
            }
            return null;
        }
    }
}