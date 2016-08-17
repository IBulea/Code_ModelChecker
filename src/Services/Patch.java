package Services;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import Model.*;
import Control.IPatch;

import javax.swing.JFileChooser;

public class Patch implements IPatch{
	
	private List<MyClass> diagramclasses;
	private List<MyClass> all_diagram_classes;
	private JFileChooser chooser = new JFileChooser();
	/*
	 * this class gets a list of the diagram classes that don't match
	 * the classes given.
	 * we construct new java files, that correspond to the diagram
	 * 
	 * 
	 */
	public Patch()
	{
		diagramclasses=new LinkedList<MyClass>();
		all_diagram_classes=new LinkedList<MyClass>();
	}
	public void SetAllDiagramClasses(List<MyClass> cl_list) {
		all_diagram_classes=cl_list;	
	}
	public void add(MyClass c)
	{
		diagramclasses.add(c);
	}
	public void startPatching(int choice) throws IOException
	{
		/*
		 * this method will create Java files in a specified directory, chosen by the user
		 */
		chooser.setCurrentDirectory(new java.io.File("d:/Users/user/Desktop/facultate/Anul 3"));
		chooser.setDialogTitle("select folder");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		String saveTo = "./";
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
		      System.out.println("getCurrentDirectory(): " 
		         +  chooser.getCurrentDirectory());
		      System.out.println("getSelectedFile() : " 
		         +  chooser.getSelectedFile().toString());
		      saveTo=chooser.getSelectedFile().toString();
		      }
		    else {
		      System.out.println("No Selection ");
		      }
		// after choosing the directory, the writing of each Java file can start
		List<MyClass> generate_classes=new LinkedList<MyClass>();
		if(choice == 1)
			generate_classes=diagramclasses;
		else if(choice ==2)
			generate_classes=all_diagram_classes;
		for (MyClass c : generate_classes)
		{
			try {
				saveTo.replace("\\", "/");
				File sourceFile   = new File(saveTo+"/" + c.getName()+".java"); // create the Java file
			    FileWriter writer = new FileWriter(sourceFile);
			    String wr="public "; // start writing the class
			    if(c.isThisInterface())
			    	wr+="interface "; 
			    else
			    	wr+="class ";
			    wr+=c.getName();
			    List<String> generals = c.getGeneral();
			    List<String> realisations=c.getRealisation();
			    List<Attribute> atrs=c.getAts();
			    List<Operation> oprs=c.getOps();
			    wr+=writeGenerals(generals);
			    wr+=writeRealisation(realisations);
			    wr+="{ \n";
			    wr+=writeAttributes(atrs);
			    wr+=writeOpearations(oprs,c);
			   	
			    wr+="}\n";	
			    writer.write(
			            wr
			    );
			    writer.close(); // close the file
				
			}catch(IOException e)
			{
				System.out.println(e);
			}
			
		}
	}
	private String writeGenerals(List<String> generals)
	{
		// write the classes that the file class extends
		String wr="";
		if(!(generals.isEmpty()))
	    {
	    	wr+=" extends ";
	    	for (int i=0; i<generals.size();i++)
	    		if(i==0)
	    			wr+=getMyClassByID(generals.get(i));
	    		else
	    			wr+=", "+getMyClassByID(generals.get(i));
	    }
		return wr;
	}
	private String writeRealisation(List<String> realisations)
	{
		// write the interfaces that the current file class implements
		String wr="";
		if(!(realisations.isEmpty()))
	    {
	    	wr+=" implements ";
	    	for (int i=0; i<realisations.size();i++)
	    		if(i==0)
	    			wr+=getMyClassByID(realisations.get(i));
	    		else
	    			wr+=", "+getMyClassByID(realisations.get(i));
	    }
		return wr;
	}
	private String writeAttributes(List<Attribute> atrs)
	{
		// write all the attributes of a class
		String wr="";
		for (Attribute a:atrs)
	    {
	    	if(a.getMultiplicity()==1)
	    		wr +="	"+a.getVisibility()+" "+getMyClassByID(a.getType())+" "+a.getName()+";\n";
	    	else
	    		wr +="	"+a.getVisibility()+" List<"+getMyClassByID(a.getType())+"> "+a.getName()+";\n";
	    	//System.out.println(wr);
	    }
		return wr;
	}
	private String writeOpearations(List<Operation> oprs, MyClass c)
	{
		// write all the operations of a class
		String wr="";
		for(Operation o:oprs)
	    {
	    	wr+="	"+o.getVisibility()+" ";
	    	String tp= "void";
	    	if(o.getName().equals(c.getName()))
	    		tp="";
	    	for(ParamPair pp :o.getParams())
	    		if(pp.getDirection().equals("return"))
	    			tp=getMyClassByID(pp.getType());
	    	wr+=tp+" "+o.getName()+"(";
	    	String params="";
	    	int ii=1;
	    	for(ParamPair pp :o.getParams()){
	    		if(!(pp.getDirection().equals("return"))){
	    			if(pp.getMultiplicity()==1){
		    			if(ii>1)
		    				params+=", " +getMyClassByID(pp.getType())+" "+pp.getName();
		    			else
		    				params+=getMyClassByID(pp.getType())+" "+pp.getName();
	    			}
	    			else
	    				if(ii>1)
		    				params+=", List<" +getMyClassByID(pp.getType())+"> "+pp.getName();
		    			else
		    				params+=" List<"+getMyClassByID(pp.getType())+"> "+pp.getName();
	    			ii++;
	    		}
	    	}
	    	wr+=params+"){\n";
	    	if(tp.equals("void"))
	    		wr+="\n	}\n";
	    	else
	    		wr+="	return *"+tp+"*;\n	}\n";	    
	    }
		return wr;
	}
	private String getMyClassByID(String type)
	{
		//get the real type of a parameter, or attribute
		// it can be another class object, or standard types;
		String actualtype=type;
		if(type.equals("Integer"))
			actualtype="int ";
		else if(type.equals("String"))
			actualtype="String ";
		else if(type.equals("Real"))
			actualtype="float ";
		else if(type.equals("Boolean"))
			actualtype="boolean ";
	    else if(type.equals("_RXVEgPMZEeCxrPjksdBlsg"))
			actualtype="Map<Key,Value> ";
	    else if(type.equals("_ynnQcO6aEeCttc32wS_Cjw"))
			actualtype="Set<T> ";
	    else if(type.equals("_23Cq0PD2EeCxrPjksdBlsg"))
			actualtype="List<T> ";
		for(MyClass mc:all_diagram_classes){
			System.out.println(mc.getID()+" aici ar trebui sa fie id");
			if(mc.getID().equals(type)){
				actualtype=mc.getName();
			System.out.println(mc.getName());}
		}
		return actualtype;
	}
}