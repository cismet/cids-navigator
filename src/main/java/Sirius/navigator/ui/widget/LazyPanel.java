package Sirius.navigator.ui.widget;

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
	Created			:	01.10.1999
	History			:

*******************************************************************************/
import java.awt.GridLayout;
import javax.swing.*;

public class LazyPanel extends JPanel
{
	private LazyGlassPane lazyGlassPane;
	private JRootPane rootPane;

	public LazyPanel(boolean blockEvents)
	{
		super();
		lazyGlassPane = new LazyGlassPane(true);
		rootPane = new JRootPane();
		rootPane.setLayeredPane(new JLayeredPane());
		rootPane.setGlassPane(lazyGlassPane);
		rootPane.getGlassPane().setVisible(blockEvents);
		this.add(rootPane);
		this.setLayout(new GridLayout(1,1));
	}

	public void setContent(JComponent content)
	{
		rootPane.setContentPane(content);
	}

	public void blockEvents(boolean blockEvents)
	{
		rootPane.getGlassPane().setVisible(blockEvents);
	}
}

