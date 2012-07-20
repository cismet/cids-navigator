/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.dialog;

import Sirius.navigator.resource.ResourceManager;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * Dies ist ein Dialog ueber den ein Datum ausgewaehlt werden kann.
 *
 * @author   Pascal Dihe
 * @version  1.0 erstellt am 01.03.2000
 */
public class DateChooser extends JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final ResourceManager resources = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final String[] days = new String[7];
    private final String[] months = new String[12];

    private int minYear = 1900;
    private Calendar calendar;

    private JSpinner yearList;
    private JComboBox monthComboBox;
    private JToggleButton dayButton;
    private JToggleButton selectedDayButton;
    private JPanel dayPanel;
    private JButton cancelButton;
    private JButton acceptButton;

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private GridBagConstraints constraints;

    private boolean accept = false;

    private ActionListener actionListener = new ButtonListener();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DateChooser object.
     */
    public DateChooser() {
        this(new JFrame(),
            org.openide.util.NbBundle.getMessage(DateChooser.class, "DateChooser.title")); // NOI18N
    }

    /**
     * Creates a new DateChooser object.
     *
     * @param  parent  Das Parent Window des DateChoosers.
     * @param  title   Der Titel des DateChoosers.
     */
    public DateChooser(final JDialog parent, final String title) {
        super(parent, title, true);
        reset();
        initDateChooser();
    }

    /**
     * Creates a new DateChooser object.
     *
     * @param  parent  DOCUMENT ME!
     * @param  title   DOCUMENT ME!
     */
    public DateChooser(final JFrame parent, final String title) {
        super(parent, title, true);
        reset();
        initDateChooser();
    }

    /**
     * Creates a new DateChooser object.
     *
     * @param  parent  DOCUMENT ME!
     * @param  title   DOCUMENT ME!
     * @param  date    DOCUMENT ME!
     */
    public DateChooser(final JDialog parent, final String title, final Date date) {
        super(parent, title, true);
        setDate(date);
        initDateChooser();
    }

    /**
     * Creates a new DateChooser object.
     *
     * @param  parent  DOCUMENT ME!
     * @param  title   DOCUMENT ME!
     * @param  date    DOCUMENT ME!
     */
    public DateChooser(final JFrame parent, final String title, final Date date) {
        super(parent, title, true);
        setDate(date);
        initDateChooser();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void initDateChooser() {
        days[0] = this.getLocalizedDay(1);
        days[1] = this.getLocalizedDay(2);
        days[2] = this.getLocalizedDay(3);
        days[3] = this.getLocalizedDay(4);
        days[4] = this.getLocalizedDay(5);
        days[5] = this.getLocalizedDay(6);
        days[6] = this.getLocalizedDay(7);

        months[0] = this.getLocalizedMonth(Calendar.JANUARY);
        months[1] = this.getLocalizedMonth(Calendar.FEBRUARY);
        months[2] = this.getLocalizedMonth(Calendar.MARCH);
        months[3] = this.getLocalizedMonth(Calendar.APRIL);
        months[4] = this.getLocalizedMonth(Calendar.MAY);
        months[5] = this.getLocalizedMonth(Calendar.JUNE);
        months[6] = this.getLocalizedMonth(Calendar.JULY);
        months[7] = this.getLocalizedMonth(Calendar.AUGUST);
        months[8] = this.getLocalizedMonth(Calendar.SEPTEMBER);
        months[9] = this.getLocalizedMonth(Calendar.OCTOBER);
        months[10] = this.getLocalizedMonth(Calendar.NOVEMBER);
        months[11] = this.getLocalizedMonth(Calendar.DECEMBER);

        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);

        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        constraints = new GridBagConstraints();

        final JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));

        // Monate =============================================================
        monthComboBox = new JComboBox(months);
        monthComboBox.setSelectedIndex(selectedMonth);
        monthComboBox.addItemListener(new MonthSelectionListener());

        controls.add(Box.createHorizontalStrut(4));
        controls.add(monthComboBox);

        // Jahre ==============================================================

        yearList = new JSpinner(new SpinnerNumberModel(selectedYear, minYear, selectedYear, 1));
        ((JSpinner.NumberEditor)yearList.getEditor()).getFormat().setDecimalSeparatorAlwaysShown(false);
        yearList.addChangeListener(new YearSelectionListener());

        controls.add(Box.createHorizontalStrut(20));
        controls.add(yearList);
        controls.add(Box.createHorizontalStrut(4));

        controls.setBorder(new EmptyBorder(10, 10, 0, 10));
        this.getContentPane().add(controls);

        // Tage Ueberschrift ==================================================

        final JPanel dayHeadingPanel = new JPanel();
        dayHeadingPanel.setLayout(new GridBagLayout());

        constraints.insets = new Insets(0, 2, 2, 2);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;

        JLabel dayLabel;
        for (int i = 0; i < days.length; i++) {
            dayLabel = new JLabel(days[i]);
            dayLabel.setPreferredSize(new Dimension(30, 30));
            dayLabel.setHorizontalAlignment(JLabel.CENTER);
            dayHeadingPanel.add(dayLabel, constraints);
            constraints.gridx++;
        }

        dayHeadingPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.getContentPane().add(dayHeadingPanel);

        // Tage ===============================================================
        dayPanel = new JPanel();
        dayPanel.setLayout(new GridBagLayout());
        updateDays();

        dayPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        this.getContentPane().add(dayPanel);

        // Buttons ============================================================

        final JPanel buttons = new JPanel();
        final GridLayout gl = new GridLayout(1, 2);
        gl.setHgap(10);
        buttons.setLayout(gl);

        acceptButton = new JButton(org.openide.util.NbBundle.getMessage(
                    DateChooser.class,
                    "DateChooser.acceptButton.title")); // NOI18N
        acceptButton.setActionCommand("apply");         // NOI18N
        acceptButton.addActionListener(actionListener);
        cancelButton = new JButton(org.openide.util.NbBundle.getMessage(
                    DateChooser.class,
                    "DateChooser.cancelButton.title")); // NOI18N
        cancelButton.setActionCommand("cancel");        // NOI18N
        cancelButton.addActionListener(actionListener);

        buttons.add(acceptButton);
        buttons.add(cancelButton);

        buttons.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.getContentPane().add(buttons);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isDateAccepted() {
        return accept;
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateDays() {
        dayPanel.removeAll();
        final Calendar cday = (Calendar)calendar.clone();
        cday.set(selectedYear, selectedMonth, 1);

        constraints.insets = new Insets(0, 2, 2, 2);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 1.0;
        // die Woche beginnt hier mit Montag, gridx bei 0 deshalb -2.
        if ((cday.get(Calendar.DAY_OF_WEEK) - 2) == -1) {
            constraints.gridx = 6;
        } else {
            constraints.gridx = cday.get(Calendar.DAY_OF_WEEK) - 2;
        }

        while (cday.get(Calendar.MONTH) == selectedMonth) {
            if (cday.get(Calendar.DAY_OF_MONTH) == selectedDay) {
                dayButton = new JToggleButton(Integer.toString(calendar.get(Calendar.DATE)), true);
                dayButton.setSelected(true);
                selectedDayButton = dayButton;
            } else {
                dayButton = new JToggleButton(Integer.toString(cday.get(Calendar.DATE)));
            }

            dayButton.setHorizontalTextPosition(AbstractButton.CENTER);
            dayButton.setVerticalTextPosition(AbstractButton.CENTER);
            dayButton.setHorizontalAlignment(AbstractButton.CENTER);
            dayButton.setVerticalAlignment(AbstractButton.CENTER);
            dayButton.setPreferredSize(new Dimension(30, 30));
            dayButton.setMargin(new Insets(1, 1, 1, 1));
            dayButton.setActionCommand("day"); // NOI18N
            dayButton.addActionListener(actionListener);

            dayPanel.add(dayButton, constraints);
            constraints.gridx++;

            if (constraints.gridx > 6) {
                constraints.gridy++;
                constraints.gridx = 0;
            }

            cday.add(Calendar.DAY_OF_MONTH, 1);
        }

        dayPanel.validate();
        dayPanel.repaint();
        this.validate();
        this.doLayout();
    }

    /**
     * Ueberlaedt toString() und liefert das ausgewaehlte Datum.
     */
    /*public String toString()
     * { String ret = selectedDay + ". " + getMonthName(this.calendar) + " " + selectedYear; return ret;}*/

    public void reset() {
        calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Liefert das ausgewaehlte Datum als "Date".
     *
     * @return  Das ausgewaehlte Datum.
     */
    public Date getDate() {
        return calendar.getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  date  DOCUMENT ME!
     */
    public void setDate(final Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        yearList.setValue(new Integer(selectedYear));
        monthComboBox.setSelectedIndex(selectedMonth);
        this.updateDays();
    }

    @Override
    public void show() {
        this.updateDays();
        this.pack();
        // NOTE: This call can not be substituted by StaticSwingTools.showDialog(this) because
        // show() method overwrites JDialog.show(). StaticSwingTools.showDialog() calls
        // setVisible(true) which internally calls JDialog show() -> endless recursion if
        // StaticSwingTools.showDialog() is called here
        super.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  date  DOCUMENT ME!
     */
    public void show(final Date date) {
        this.setDate(date);
        this.pack();
        StaticSwingTools.showDialog(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   calendarMonth  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getLocalizedMonth(final int calendarMonth) {
        Date date = null;
        String monthName = null;

        final Calendar cal = Calendar.getInstance();
        cal.set(2004, calendarMonth, 1);
        date = cal.getTime();

        final SimpleDateFormat dateF = new SimpleDateFormat("MMMM", resources.getLocale()); // NOI18N

        monthName = dateF.format(date);
        return monthName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   calendarDay  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getLocalizedDay(final int calendarDay) {
        Date date = null;
        String dayName = null;

        final Calendar cal = Calendar.getInstance();
        cal.set(2001, 0, calendarDay);
        date = cal.getTime();

        final SimpleDateFormat dateF = new SimpleDateFormat("E", resources.getLocale()); // NOI18N

        dayName = dateF.format(date);
        return dayName;
    }

    /**
     * .........................................................................
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final DateChooser dc = new DateChooser();

        ((JSpinner.NumberEditor)dc.yearList.getEditor()).getFormat().applyPattern("#"); // NOI18N

        System.out.println(dc.yearList.getEditor().getClass());
        dc.show();

        System.out.println(new SimpleDateFormat().format(dc.getDate()));
        System.exit(0);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class YearSelectionListener implements ChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void stateChanged(final ChangeEvent e) {
            final Calendar tmpCalendar = (Calendar)calendar.clone();
            selectedYear = ((Integer)yearList.getValue()).intValue();

            // In einem Schaltjahr hat der Februar 29 Tage. Ist der 29.
            // Februar ausgewaehlt und es wird ein Jahr selektiert das kein
            // Schaltjahr ist, muss der 28. Februar ausgewaehlt werden.
            tmpCalendar.set(selectedYear, selectedMonth, 1);
            // NavigatorLogger.printMessage(tmpCalendar.getActualMaximum(calendar.DAY_OF_MONTH));
            if (selectedDay > tmpCalendar.getActualMaximum(calendar.DAY_OF_MONTH)) {
                selectedDay = tmpCalendar.getActualMaximum(calendar.DAY_OF_MONTH);
            }
            calendar.set(selectedYear, selectedMonth, selectedDay);

            updateDays();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MonthSelectionListener implements ItemListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void itemStateChanged(final ItemEvent ie) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                final Calendar tmpCalendar = (Calendar)calendar.clone();
                selectedMonth = monthComboBox.getSelectedIndex();

                // Wenn der 31. Tag eines Monats ausgewaehlt ist und es wird ein
                // Monat selektiert, der nur 30 Tage hat, wird der letzte Tag dieses
                // Monats ausgewaehlt, also der 30.
                tmpCalendar.set(selectedYear, selectedMonth, 1);
                // NavigatorLogger.printMessage(tmpCalendar.getActualMaximum(calendar.DAY_OF_MONTH));
                if (selectedDay > tmpCalendar.getActualMaximum(calendar.DAY_OF_MONTH)) {
                    selectedDay = tmpCalendar.getActualMaximum(calendar.DAY_OF_MONTH);
                }
                calendar.set(selectedYear, selectedMonth, selectedDay);

                updateDays();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ButtonListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("day"))           // NOI18N
            {
                if (e.getSource() instanceof JToggleButton) {
                    selectedDayButton.setSelected(false);
                    selectedDayButton = (JToggleButton)e.getSource();
                    selectedDayButton.setSelected(true);
                    final Integer intTmp = new Integer(selectedDayButton.getText());
                    selectedDay = intTmp.intValue();
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    dayPanel.repaint();
                }
            } else if (e.getActionCommand().equals("cancel")) // NOI18N
            {
                accept = false;
                // reset();
                // updateDays();
                DateChooser.this.setVisible(false);
                DateChooser.this.dispose();
            } else if (e.getActionCommand().equals("apply")) // NOI18N
            {
                accept = true;
                DateChooser.this.setVisible(false);
                DateChooser.this.dispose();
            }
        }
    }
}
