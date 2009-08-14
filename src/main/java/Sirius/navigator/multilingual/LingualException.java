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

	Programmers		:	Annen Thomas

 	Project				:	WuNDA 2
 	Filename			:	LingualException.java
	Version				:	1.0
 	Purpose				: Exception-handling for LingualTest.
	Created				:	09.02.2001
	History				:

*******************************************************************************/

package Sirius.navigator.multilingual;

import Sirius.navigator.NavigatorLogger;

// XXX remove class, migrate to ResourceManager

/**
 * @deprecated migrate to ResourceManager
 */
public class LingualException extends Exception
{
	public LingualException(String message, boolean stacktr, boolean exit)
	{
		super(message);
		m_Message = new String(message);
		m_StackTr = stacktr;
		m_Exit    = exit;
	}

	public void printError()
	{
		NavigatorLogger.printMessage("<LingualException caught>");
		NavigatorLogger.printMessage(m_Message);
		if(m_StackTr)
			printStackTrace();
		if(m_Exit)
			System.exit(1);
	}
	private String 	m_Message;
	private boolean	m_StackTr;
	private boolean	m_Exit;
}

