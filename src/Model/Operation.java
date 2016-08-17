package Model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Operation{

	private String name;
	private List<ParamPair> parameters;
	private String visibility;
	//private boolean isAbstract;
	public Operation(String name, List<ParamPair>params,String v)
	{
		this.name=name;
		parameters=new LinkedList<ParamPair>();
		parameters = params;
		visibility = v;
		//System.out.println(this.name+"\n Params "+parameters+"\n	Visibility:"+visibility);
		//isAbstract=false;
	}
	public String getName()
	{
		return name;
	}
	public List<ParamPair> getParams()
	{
		return parameters;
	}
	public String getVisibility()
	{
		return visibility;
	}
	/*public void setAbstract()
	{
		isAbstract=true;
	}*/
}
