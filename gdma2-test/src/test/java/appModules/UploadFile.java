package appModules;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class UploadFile {
	// This method will set any parameter string to the system's clipboard
	
	public static void setClipboardData(String string)
	{
		StringSelection stringSelection= new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

}
