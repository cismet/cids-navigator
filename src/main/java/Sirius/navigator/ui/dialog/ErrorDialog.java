package Sirius.navigator.ui.dialog;

/*******************************************************************************

 	Copyright (c)	:	EIG (Environmental Informatics Group)
						http://www.htw-saarland.de/eig
						Prof. Dr. Reiner Guettler
						Prof. Dr. Ralf Denzer
 						
						HTWdS 
						Hochschule fuer Technik und Wirtschaft des Saarlandes
						Goebenstr. 40
 						66117 Saarbruecken
 						Germany

	Programmers		:	Pascal 

 	Project			:	WuNDA 2
 	Filename		:	
	Version			:	1.0
 	Purpose			:	
	Created			:	05.07.2000
	History			:

*******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import Sirius.navigator.NavigatorLogger;
import Sirius.navigator.resource.*;
import org.apache.log4j.lf5.util.Resource;
//import Sirius.navigator.connection.ConnectionHandler;
import java.util.ResourceBundle;


public class ErrorDialog extends JDialog implements ActionListener
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

	//_TA_public final static String WARNING = "Warnung";
	public final static String WARNING = I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.WARNING");
	
	//_TA_public final static String ERROR = "Kritischer Fehler";
	public final static String ERROR = I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.ERROR");
	
	//_TA_protected String errorMessage = "Es ist ein kritischer Fehler aufgetreten";
	protected String errorMessage = I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.errorMessage");
	protected String stackTrace = null;
	protected String errorType = ERROR;

	protected JLabel errorLabel;
	protected JPanel detailsPanel;
	protected JTextArea detailsTextArea;	
	protected JButton buttonIgnore, buttonExit, buttonDetails;
	
	public ErrorDialog()
	{
		//_TA_super(new JFrame(), "Kritischer Fehler", true);
		super(new JFrame(), ERROR, true);
		initErrorDialog();
	}
	
	public ErrorDialog(String errorMessage, String errorType)
	{
		super(new JFrame(), errorType, true);
		this.errorMessage = errorMessage;
		this.setErrorType(errorType);
		initErrorDialog();
	}
	
	public ErrorDialog(String errorMessage, String stackTrace, String errorType)
	{
		super(new JFrame(), errorType, true);
		this.errorMessage = errorMessage;
		this.stackTrace = stackTrace;
		this.setErrorType(errorType);
		initErrorDialog();
	}
	
	protected void initErrorDialog()
	{
		//this.setLocationRelativeTo(this.getParent());
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(10,10,10,10));
		GridBagConstraints constraints = new GridBagConstraints();

		// ICON ================================================================
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		constraints.gridy = 0;
		constraints.gridx = 0;
		
		JLabel errorIcon;
		if(errorType.equals(ERROR))
			errorIcon = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
		else 
			errorIcon = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
		
		errorIcon.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(10,10,10,10)));
		contentPane.add(errorIcon, constraints);

		// MESSAGE =============================================================
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.5;
		constraints.gridx++;
		errorLabel = new JLabel(errorMessage);
		errorLabel.setBorder(new EmptyBorder(20,20,20,20));
		contentPane.add(errorLabel, constraints);
		
		// BUTTONS =============================================================
		constraints.insets = new Insets(20, 0, 10, 0);
		constraints.gridwidth = 2;
		constraints.gridy = 1;
		constraints.gridx = 0;	
		JPanel buttonPanel = new JPanel(new GridLayout(1,3,10,10));	
		
		//_TA_buttonIgnore = new JButton("Ignorieren");
		buttonIgnore = new JButton(I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.buttonIgnore.title"));
		//_TA_buttonIgnore.setMnemonic('I');
		buttonIgnore.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.buttonIgnore.mnemonic").charAt(0));
		buttonPanel.add(buttonIgnore);
		
		//_TA_buttonExit = new JButton("Beenden");
		buttonExit = new JButton(I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.buttonExit.title"));
		//_TA_buttonExit.setMnemonic('B');
		buttonExit.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.buttonExit.mnemonic").charAt(0));
		buttonExit.setActionCommand("exit");
		buttonExit.addActionListener(this);	
		buttonPanel.add(buttonExit);
		
		if(errorType.equals(WARNING))
		{
			buttonIgnore.setActionCommand("ignore");
			buttonIgnore.addActionListener(this);
		}
		else
		{
			buttonIgnore.setEnabled(false);
		}
		
		//_TA_buttonDetails = new JButton("Details");
		buttonDetails = new JButton(I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.buttonDetails.title"));
		//_TA_buttonDetails.setMnemonic('D');	
		buttonDetails.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.buttonDetails.mnemonic").charAt(0));
		buttonPanel.add(buttonDetails);
		
		contentPane.add(buttonPanel, constraints);
		
		// DETAILS =============================================================
		if (stackTrace != null)
		{		
			buttonDetails.setActionCommand("details");
			buttonDetails.addActionListener(this);
			
			constraints.insets = new Insets(0, 0, 0, 0);
			constraints.gridy++;
			constraints.weightx = 1.0;
			constraints.weighty = 1.0;
			detailsTextArea = new JTextArea(stackTrace, 4, 20); 
			detailsPanel = new JPanel(new GridLayout(1,1));
			//detailsPanel.setBorder(new EmptyBorder(10,10,10,10));
			detailsPanel.add(new JScrollPane(detailsTextArea));
			detailsPanel.setVisible(false);
			contentPane.add(detailsPanel, constraints);
		}
		else
		{
			buttonDetails.setEnabled(false);
		}
		
		
		
		this.setContentPane(contentPane);
		this.pack();	
		
		Sirius.navigator.tools.MetaToolkit.centerWindow(this);
	}
	
	
	protected void setErrorType(String errorType)
	{
		if(errorType.equals(WARNING) || errorType.equals(ERROR))
			this.errorType = errorType;
		else
			this.errorType = ERROR;
		
		this.setTitle(errorType);
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("exit"))
		{
			if(errorType.equals(ERROR))
			{
				System.exit(1);
			}
			else
			{
				/*
				 _TA_JOptionPane optionPane = new JOptionPane("<html><center><p>Moechten Sie den</p><p>Navigator wirklich schliessen?</p></center></html>", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, new String[]{"Ja", "Nein"}, null);
				 _TA_JDialog dialog = optionPane.createDialog(this, "Programm beenden");	
				*/
				JOptionPane optionPane = new JOptionPane(
                                        I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.actionPerformed().optionPane.message"),
                                        JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION,
                                        null,
                                        new String[]{
                                            I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.actionPerformed().optionPane.yes"),
                                            I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.actionPerformed().optionPane.no")},
                                        null);

				JDialog dialog = optionPane.createDialog(this,
                                        I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.actionPerformed().dialog.title"));
				dialog.show();
				
				//_TA_if(optionPane.getValue().equals("Ja"))
				if(optionPane.getValue().equals(
                                        I18N.getString("Sirius.navigator.ui.dialog.ErrorDialog.actionPerformed().optionPane.yes")))
				{
					if(NavigatorLogger.VERBOSE)NavigatorLogger.printMessage("<NAV> Navigator closed()");
					System.exit(1);
				}
			}
		}
		else if(e.getActionCommand().equals("ignore"))
		{
			this.dispose();
		}
		else if(e.getActionCommand().equals("details"))
		{
			buttonDetails.setEnabled(false);
			detailsPanel.setVisible(true);
			this.pack();
		}
		
	}

}

