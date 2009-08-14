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
 	Filename			:	LingualTest.java
	Version				:	1.0
 	Purpose				: Tests the class StringLoader. It can be started with 
									java -cp . LingualTest <[-rf RessourceFile] | [-dir Directory]>  
									You have to start either with a ressource-file or a directory.
			
	Created				:	09.02.2001
	History				:

*******************************************************************************/
package Sirius.navigator.multilingual;

import java.io.*;
import java.util.*;

import Sirius.navigator.NavigatorLogger;
// XXX remove class, migrate to ResourceManager

/**
 * @deprecated migrate to ResourceManager
 */
public class LingualTest
{
	public LingualTest(String[] args)
	{
		
			if(args.length != 2)
				usage();
				
			m_Filename 		= new String(args[0]);
			m_StartToken	= new String(args[1]);
			m_FileContent = new String("");
			m_numJavaFiles= 0;

			try
			{
					NavigatorLogger.printMessage("Filename    : " + m_Filename);
					m_Input = new File(m_Filename);
					if(!m_Input.exists())
						throw new LingualException("File: " + m_Filename + " does not exists!" , false, true);
			}
			catch(LingualException le)
			{
				le.printError();
			}
			catch(Exception e)
			{
				NavigatorLogger.printMessage("FATAL_ERROR <construction error>");
				System.exit(1);
			}
	}

	public void test()
	{
		NavigatorLogger.printMessage("\n<LingualTest: test()>");
		NavigatorLogger.printMessage("--------------------");
		try
		{
					testDirectory(m_Input);
					NavigatorLogger.printMessage("<Number of java-files in directories: " + m_numJavaFiles);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			NavigatorLogger.printMessage("FATAL_ERROR");
			System.exit(1);
		}
	}

	/**
	 * Short concise description.
	 * Additional verbose description.
	 * @return description.
	 * @exception LingualException description.
	 * @see package.class
	 */
	private void testFile() throws LingualException
	{
		//NavigatorLogger.printMessage("<FileTest for: "+m_Input.getAbsolutePath());
		NavigatorLogger.printMessage("<FileTest for: "+m_Input.getName());
		try
		{
			m_FileReader 				= new FileReader(m_Input);
			BufferedReader	in 	= new BufferedReader(m_FileReader);
			/*
			 * Read the hole file content into a single string.
			 */
			String line = new String();
			while((line = in.readLine()) != null)
					m_FileContent += line+"\n";
			
			if(m_FileContent == null) //LingualException ist total ueberfluessig!
				throw new LingualException("Couldn't read file content" , false, true);
				
			StringTokenizer tokenizer = new StringTokenizer(m_FileContent," \"");
			String 		token 		= new String();
			String[] 	tokenArray;
		
			for(int i=0; tokenizer.hasMoreTokens(); i++)
			{
				token = tokenizer.nextToken();
				if(token.startsWith(m_StartToken))
				{
					if(token.endsWith("ARRAY"))
					{
						tokenArray = StringLoader.getStringArray(token);
						/*----------------------------------------------------------------------------------*/
						if(tokenArray != null)
						{
							NavigatorLogger.printMessage("Values from the String-Array: "+token+" are: ");
							NavigatorLogger.printMessage("\t|");
							for(int j=0; j < tokenArray.length; j++)
							{
								if(j < 9)
									NavigatorLogger.printMessage("\t+---- The  " + (j+1) + ". value is: "+tokenArray[j]);
								else
									NavigatorLogger.printMessage("\t+---- The " + (j+1) + ". value is: "+tokenArray[j]);
							}
							NavigatorLogger.printMessage(" ");
						}
						/*-----------------------------------------------------------------------------------*/
					}
					else
					{
						if(token.endsWith("Mnemonic"))
						{
							NavigatorLogger.printMessage("Key  : "+token);
							NavigatorLogger.printMessage("Value: "+StringLoader.getMnemonicChar(token));
						}
						else
						{
							NavigatorLogger.printMessage("Key  : "+token);
							NavigatorLogger.printMessage("Value: "+StringLoader.getString(token));
						}
					}
				}
			}
			m_FileContent = "";
			
			
			NavigatorLogger.printMessage("--------------------------------------------");
			NavigatorLogger.printMessage("Number of MissingResourceExceptions: "+
												 StringLoader.numberOfMissingResourceExceptions()+"...");
			NavigatorLogger.printMessage("--------------------------------------------");
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			NavigatorLogger.printMessage("FATAL_ERROR <fileoperarion error (IO)>");
			System.exit(1);
		}
		catch(LingualException le)
		{
			le.printError();
		}
	}
	
	private void testDirectory(File file) throws LingualException
	{
		if(file.isDirectory())
		{
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++)
				testDirectory(files[i]);
		}
		else
		{
			if((file.getName()).endsWith(".java") && !(file.getName()).endsWith("LingualTest.java"))
			{
				m_Input = new File(file.getAbsolutePath());
				testFile();
				m_numJavaFiles++;
			}
		}
	}
	
	private void usage()
	{
		NavigatorLogger.printMessage("usage:\n java LingualTest <directory> <token>");
		NavigatorLogger.printMessage(" token : Defines the starttoken e.q. STL@ ---> STL@helloWorldString resolves to helloWorldString");
		System.exit(0);
	}
	
	
	private 			boolean 		m_OutputToFile;	/* Is the output-option set, results will be written in the specified file. */
	private				int					m_numJavaFiles;
	private 			String			m_Filename;			/* Filename of input-file.																									*/
	private 			String			m_FileContent;	/* File contet as a string.																									*/
	private final String			m_StartToken;		/* Marks the beginning of a token, key.																			*/
	private 			File				m_Input;				/* Fileobject for input, it can be a file or directory.                     */
	private 			FileWriter	m_FileWriter;		/* FileWriter for Fileoperations.                                           */
	private 			FileReader	m_FileReader;		/* FileWriter for Fileoperations.                                           */
	
	
	
	/*
	 * Main method.
	 */
	public static void main(String[] args)
	{
		LingualTest lt = new LingualTest(args);
		lt.test();
	}
}

