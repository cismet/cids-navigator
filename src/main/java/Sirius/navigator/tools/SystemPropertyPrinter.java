package Sirius.navigator.tools;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
*/
public final class SystemPropertyPrinter
{

	public final static void main(String args[])
	{
		print();
	}

	public final static void print()
	{
		System.out.println(getString());
	}
	
	public final static String getString()
	{
		StringBuffer buffer = new StringBuffer();
		String[][] stringArray = getStringArray();
		
		for(int i = 0; i < stringArray.length; i++)
		{
			buffer.append(stringArray[i][0]);
			buffer.append(": \t");
			buffer.append(stringArray[i][1]);		
			buffer.append("\n");
		}
	
		return buffer.toString();
	}
	
	public final static String[][] getStringArray()
	{
		String[][] pArray = new String[25][2];
		
		pArray[0][0] = "java.version";
		pArray[0][1] = getProperty(pArray[0][0]);
		
		pArray[1][0] = "java.vendor";
		pArray[1][1] = getProperty(pArray[1][0]);
		
		pArray[2][0] = "java.vendor.url";
		pArray[2][1] = getProperty(pArray[2][0]);
		
		pArray[3][0] = "java.home";
		pArray[3][1] = getProperty(pArray[3][0]);
		
		pArray[4][0] = "java.vm.specification.version";
		pArray[4][1] = getProperty(pArray[4][0]);
		
		pArray[5][0] = "java.vm.specification.vendor";
		pArray[5][1] = getProperty(pArray[5][0]);
		
		pArray[6][0] = "java.vm.specification.name";
		pArray[6][1] = getProperty(pArray[6][0]);
		
		pArray[7][0] = "java.vm.version";
		pArray[7][1] = getProperty(pArray[7][0]);
		
		pArray[8][0] = "java.vm.vender";
		pArray[8][1] = getProperty(pArray[8][0]);
		
		pArray[9][0] = "java.vm.name";
		pArray[9][1] = getProperty(pArray[9][0]);
		
		pArray[10][0] = "java.specification.version";
		pArray[10][1] = getProperty(pArray[10][0]);
		
		pArray[11][0] = "java.specification.vendor";
		pArray[11][1] = getProperty(pArray[11][0]);
		
		pArray[12][0] = "java.specification.name";
		pArray[12][1] = getProperty(pArray[12][0]);
		
		pArray[13][0] = "java.class.version";
		pArray[13][1] = getProperty(pArray[13][0]);
		
		pArray[14][0] = "java.class.path";
		pArray[14][1] = getProperty(pArray[14][0]);
		
		pArray[15][0] = "java.ext.dirs";
		pArray[15][1] = getProperty(pArray[15][0]);
		
		pArray[16][0] = "os.name";
		pArray[16][1] = getProperty(pArray[16][0]);
		
		pArray[17][0] = "os.arch";
		pArray[17][1] = getProperty(pArray[17][0]);
		
		pArray[18][0] = "os.version";
		pArray[18][1] = getProperty(pArray[18][0]);
		
		pArray[19][0] = "file.separator";
		pArray[19][1] = getProperty(pArray[19][0]);
		
		pArray[20][0] = "path.separator";
		pArray[20][1] = getProperty(pArray[20][0]);
		
		pArray[21][0] = "line.separator";
		pArray[21][1] = getProperty(pArray[21][0]);
		
		pArray[22][0] = "user.name";
		pArray[22][1] = getProperty(pArray[22][0]);
		
		pArray[23][0] = "user.home";
		pArray[23][1] = getProperty(pArray[23][0]);
		
		pArray[24][0] = "user.dir";
		pArray[24][1] = getProperty(pArray[24][0]);
	
		return pArray;	
	}
	
	private final static String getProperty(String property)
	{
		try
		{
			return System.getProperty(property, "null");
		}
		catch(SecurityException se)
		{
			return "Access denied";
		}
		catch(Exception e)
		{
			return "null";
		}	
	}


}

