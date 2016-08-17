package View;

import java.io.File;

import javax.swing.JFileChooser;

public class LoadFiles {

	private JFileChooser chooser = new JFileChooser();
	public File[] selectfiles()
	{
		/*
		 * choose the files that will be inputed into the application
		 */
		File[] files = null;
		chooser.setMultiSelectionEnabled(true);
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(new java.io.File("d:/Users/user/Desktop/facultate/Anul 3"));
		
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			files = chooser.getSelectedFiles();
			/*for(int i = 0; i< files.length;i++)
				System.out.println(files[i].getAbsolutePath());*/
				
		}
		else
			System.out.println("No files chosen");
		return files;
	}
}
