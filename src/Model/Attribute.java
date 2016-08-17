package Model;

public class Attribute {
	private String name;
	private String id;
	private String type;
	private String visibility;
	private int multiplicity;
	public String getName()
	{
		return name;
	}
	public String getID()
	{return id;}
	public String getType()
	{
		return type;
	}
	public String getVisibility()
	{return visibility;}
	public int getMultiplicity()
	{
		return multiplicity;
	}
	public void setMultiplicity(int m)
	{
		multiplicity=m;
	}
	public String getRealType()
	{
		String actualtype="";
		if(type.equals("Integer"))
			actualtype="int";
		else if(type.equals("String"))
			actualtype="String";
		else if(type.equals("Real"))
			actualtype="float";
		else if(type.equals("Boolean"))
			actualtype="boolean";
	    else if(type.equals("_RXVEgPMZEeCxrPjksdBlsg"))
			actualtype="Map<Key,Value>";
	    else if(type.equals("_ynnQcO6aEeCttc32wS_Cjw"))
			actualtype="Set<T>";
	    else if(type.equals("_23Cq0PD2EeCxrPjksdBlsg"))
			actualtype="List<T>";
		return actualtype;
	}
	public Attribute(String name, String id,String t,String v)
	{
		this.name= name;
		this.id=id;
		type=t;
		visibility=v;
		multiplicity=1;
		//System.out.println(this.name+this.id+type+visibility);
	}

}
