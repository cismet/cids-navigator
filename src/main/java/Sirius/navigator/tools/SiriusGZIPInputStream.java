package Sirius.navigator.tools;

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
import java.io.*;
import java.util.zip.*;

public class SiriusGZIPInputStream extends GZIPInputStream
{
	protected Deflater deflater = new Deflater();
	
	public SiriusGZIPInputStream(InputStream in) throws IOException
	{
		super(in);
	}
	
	public SiriusGZIPInputStream(InputStream in, int size) throws IOException
	{
		super(in, size);
	}

	public int read(byte[] b,int offset,int length) throws IOException   
	{
		byte[] buffer = new byte[1024];
		deflater.reset();
		deflater.setInput(b,offset,length);
		deflater.finish();
		
		if ( length < 128 ) 
		{
			deflater.setLevel(deflater.NO_COMPRESSION);
		} 
		else 
		{
			deflater.setLevel(deflater.DEFAULT_COMPRESSION);
		}
		
		int deflated = -1;
		
		while (!deflater.finished()) 
		{
			deflated = deflater.deflate(buffer);
			super.read(buffer,0,deflated);
		}
		
		return deflated;
	}
}

