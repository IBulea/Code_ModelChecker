package Services;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import Control.ILoadClasses;
public class LoadClasses implements ILoadClasses{
	private File[] files;
	private List<String> names = new LinkedList<String>();
	private List<Class> classes =  new LinkedList<Class>();
	private URL[] urls ;
	private URLClassLoader cl;
	public LoadClasses(File[] f)
	{
		files = f;
		urls = new URL[files.length];
		
	}

	public void load()
	{
		/*
		 * this method loads the bytecode classes into the application
		 */
		try {
		    // Convert File to a URL
			for(int i =0; i <files.length;i++){
				File ff=addtourls(files[i].getAbsolutePath());
				urls[i]= ff.toURL();
			}
		    // Create a new class loader with the directory
		    cl = new URLClassLoader(urls);
		    for(int i = 0; i<names.size();i++){
		    	Class cls = cl.loadClass(names.get(i));
		    	System.out.println(cls.getName());
		    	classes.add(cls);
		    	System.out.println(classes.size());
		    }
		} catch (ClassNotFoundException e) {
			System.out.println("Class was not found");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Class> getClasses()
	{
		return classes;
	}
	public void addtonames(String a)
	{
		//we add the name of the bytecode class to the list of classes to be loaded
		String[] nn = a.split("\\.");
		//System.out.println(nn[0].replace("\\", "."));
		
		//we replace the '\' character from the url with the '.' character, so we may load the class
		names.add(nn[0].replace("\\", "."));
	}
	public File addtourls(String string)
	{
		/*
		 * location of the .class files is in the bin folder of an application
		 * the bin folder contains either .class files themselves, or packages containing .class files
		 * the package name is included in the name of the class to be loaded
		 * Thus, everyting in the URL after the bin folder is considered to be the name of the bytecode class
		 */
		int i = string.lastIndexOf("\\bin\\");
		String[] a =  {string.substring(0, i+5), string.substring(i+4)};
		/*System.out.println(a[0]);
		System.out.println(a[1]);
		System.out.println("\n");*/
		File ff  = new File(a[0]);
		addtonames(a[1].substring(1));
		return ff;
	}
	
}
