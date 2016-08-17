package Model;

import java.util.LinkedList;
import java.util.List;

public class MyClass {

	private List<Operation> operations;
	private List<Attribute> attributes;
	private String name;
	private String id;
	private boolean isInterface;
	private List<String> generalisation =  new LinkedList<String>();
	private List<String> realisation = new LinkedList<String>();
	public MyClass(List<Operation> op,List<Attribute> at,String n, String i)
	{
		
		this.name=n;
		this.id=i;
		System.out.println(name +"<-  name");
		//System.out.println(name + " "+id +" constructor");
		operations=new LinkedList<Operation>();
		attributes=new LinkedList<Attribute>();
		operations= op;
		attributes=at;
		isInterface=false;
	}
	public void addGeneral(String g){
		generalisation.add(g);
	}
	public void addRealisation(String r){
		realisation.add(r);
	}
	public List<String> getGeneral()
	{
		return generalisation;
	}
	public List<String> getRealisation()
	{
		return realisation;
	}
	public void setInterface(boolean t)
	{
		isInterface=t;
	}
	public boolean isThisInterface()
	{
		return isInterface;
	}
	public String getName()
	{
		return name;
	}
	public String getID()
	{
		return id;
	}
	public List<Operation> getOps()
	{
		return operations;
	}
	public List<Attribute> getAts()
	{
		return attributes;
	}
	//private String ContainingPackage;
	//private boolean hasInterface;
	//	private booelan isStatic;
}
