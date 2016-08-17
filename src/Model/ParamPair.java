package Model;

public class ParamPair {

	private String type;
	private String name;
	private String direction;
	private int multiplicity;
	public ParamPair(String t, String d)
	{
		direction =d;
		type=t;
		multiplicity=1;
		name="";
	}
	public int getMultiplicity()
	{
		return multiplicity;
	}
	public void setMultiplicity(int s)
	{
		multiplicity=s;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String n)
	{
		this.name=n;
	}
	public String getType()
	{
		return type;
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
	public String getDirection()
	{
		return direction;
	}
}
