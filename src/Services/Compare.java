package Services;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Model.*;
import Control.*;
public class Compare implements ICompare {
	private List<Class> classes =  new LinkedList<Class>();
	private List<MyClass> diagramclasses = new LinkedList<MyClass>();
	private Method[] meths;
	private Field[] fields;
	private IPatch patch;
	private boolean methods_are_fine=false; //for each class, this field will determine the validity of its methods after comparison
	public Compare(List<Class> cl, List<MyClass> dcl)
	{
		classes =cl;
		diagramclasses=dcl;
		patch=new Patch();
		patch.SetAllDiagramClasses(diagramclasses);
	}
	public String compare(){
		/*
		 * the comparison between the bytecode and the diagram classes starts here
		 * First we check if the class id found in the diagram, then its fields, constructors and methods 
		 */
		String result="";
		for (Class c : classes)
		{	
			methods_are_fine=false;
			result+=" * *  Class " +c.getSimpleName()+" * * \n" ;
			String cl_res = checkClass(c);
			if(cl_res.equals(""))
			{
				String field_result=checkfields(c.getDeclaredFields(),getDiagramClassByName(c.getSimpleName()));
				result+=field_result;
				String constructor_check=checkContructors(c.getConstructors(),getDiagramClassByName(c.getSimpleName()));
				result+=constructor_check;
				String method_result=checkmethods(c.getDeclaredMethods(),getDiagramClassByName(c.getSimpleName()));
				result+=method_result;
			
				
				if(!( methods_are_fine) || !(field_result.equals("")) || !(constructor_check.equals("")))
					patch.add(getDiagramClassByName(c.getSimpleName()));
				result+="\n------------\n";
			}
			else{
				result +=cl_res+"\n";
				for(MyClass dcl:diagramclasses)
					patch.add(dcl);
			}
		}
		return result;
	}
	public void patch(int i)
	{
		try {
			patch.startPatching(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String checkClass(Class c)
	{
		/*
		 * check if a class or an interface is in the diagram or not
		 */
		String result = "Class "+c.getSimpleName()+" is not in the diagram\n";
		for(MyClass dcl:diagramclasses)
			if(c.getSimpleName().equals(dcl.getName()))
				if(c.isInterface())
					if(dcl.isThisInterface())
						result="";
					else
						result="Interface "+c.getSimpleName()+"is not in the diagram\n";
				else
					if(dcl.isThisInterface())
						result="Class "+c.getSimpleName()+"is not in the diagram\n";
					else
						result="";
		return result;
	}
	public String checkfields(Field[] f,MyClass dcl)
	{
		/*
		 * Starts checking the fields of the bytecode class
		 * Input: Fields[] f ->  the fields of a bytecode class
		 * 		  MyClass dcl -> a diagram class for comparison
		 * Output:  a strung representing the results of the comparison
		 */
		String res="";
		List<Attribute> ats=dcl.getAts();
		if(f.length!=ats.size())
			res+="The class " + dcl.getName()+" does not have the same number of fields as in the diagram. \n";
		for(Field ff : f){
			res+=checkFieldName(ff,dcl);
			res+=checkFieldType(ff,dcl);
			res+=chackFieldVisibility(ff,dcl);
		}		
		return res;
	}
	public String checkFieldName(Field ff,MyClass dcl)
	{
		/*
		 * Check if the name of a bytecode field matches in the diagram class
		 */
		String res ="";
		res+="The field " + ff.getName()+" does not exist in the diagram class "+dcl.getName()+"\n";
		List<Attribute> ats=dcl.getAts();
		for(Attribute at:ats)
			if(ff.getName().equals(at.getName()))
				res="";
		return res;
	}
	public String checkFieldType(Field ff, MyClass dcl)
	{
		/*
		 *  Check if the type of a bytecode field matches in the diagram class
		 */
		String res ="";
		res+="The field " + ff.getName()+" does not have the same type as in the diagram.\n";
		List<Attribute> ats=dcl.getAts();
		Class<?> ty ;
		for(Attribute at:ats)
		{
			ty= ff.getType();
			String actualtype=getActualType(ty);
			if(actualtype.equals(at.getRealType()))
				res="";
		}
		return res;
		
	}
	public String chackFieldVisibility(Field ff,MyClass dcl)
	{
		/*
		 *  Check if the visibility of a bytecode field matches in the diagram class
		 */
		String res ="";
		res+="The field " + ff.getName()+" does not have the same visibility as in the diagram.\n";
		List<Attribute> ats=dcl.getAts();
		for(Attribute at:ats)
		{
			String modifier = Modifier.toString(ff.getModifiers());
			//System.out.println("the modifiers of class attribute " + ff.getName()+" is "+modifier+"\n");
			if(modifier.contains(at.getVisibility()))
				res="";
			else
				res= "The field " + ff.getName()+ " is not " + at.getVisibility()+" like in the diagram \n"; 
		}
		return res;
	}
	public String checkContructors(Constructor[] c, MyClass dcl)
	{
		/*
		 * This method checks the constructors of a diagram class against a diagram class
		 * Input: Constructor[] c ->  the list of constructors from the bytecode class;
		 *        MyClass dcl - > the diagram class
		 * Output: a string containing the result of the comparison
		 */
		String res="";
		List<Operation> ops=dcl.getOps();
		int diagram_class_constructors=getNumberofConstructorsInDiagramClass(dcl);
		//count the implicit constructor anyway
		if(diagram_class_constructors==0)
			diagram_class_constructors=1;
		if(c.length!=diagram_class_constructors)
			res="The diagram class " + dcl.getName()+" doesn't have the same number of constructors as the given class.\n";
		for (Constructor cc:c)
			for(Operation op:ops)
				if(op.getName().equals(cc.getName()))
					res+=checkConstructorParameters(cc,op);
		//System.out.println("Constructors for class: "+c.length+" -> for diagram: "+diagram_class_constructors+"\n");
		return res;
	}
	public int getNumberofConstructorsInDiagramClass(MyClass dcl)
	{
		/*
		 * count the constructor methods in the diagram class;
		 */
		List<Operation> ops=dcl.getOps();
		int diagram_class_constructors=0;
		for(Operation oo:ops)
			if(oo.getName().equals(dcl.getName()))
				diagram_class_constructors++;
		return diagram_class_constructors;
	}
	public String checkConstructorParameters(Constructor cc,Operation op)
	{
		/*
		 * Check the parameters of a constructor by comparing them with the constructor operation from diagram
		 */
		String result="";
		List<ParamPair> dp=op.getParams();
		Parameter[] pm = cc.getParameters();
		int diagramParams=dp.size();
		if(cc.getParameterCount()!=diagramParams)
			result+="The Constructors "+cc.getName()+" from diagram and class file have different numbers of parameters\n";
		for (Parameter pp:pm){
			result+=checkParamType(pp,dp);
		}
		return result;
	}
	public String checkmethods(Method[] m,MyClass dcl)
	{
		/*
		 * We check every method from the bytecode class with every method from the diagram class
		 */
		String res="";
		List<Operation> ops=dcl.getOps();
		//System.out.println("The number of methods in class " + dcl.getName()+" -> "+m.length);
		if(m.length!=(ops.size()-getNumberofConstructorsInDiagramClass(dcl))){ //check the number of methods, without constructors
			res+="The class " + dcl.getName()+" does not have the same number o methods as in the diagram. \n";
		}
		boolean found_wrong_method=false;
		for(Method mm:m)
		{
			res+="    Method "+ mm.getName()+":\n";
			boolean exists=false;
			for(Operation op:ops)
				if(mm.getName().equals(op.getName())){ // we have found the corresponding method in the diagram class
					String paramResults=checkParameters(mm,op);
					if(!(paramResults.equals("")))
						found_wrong_method=true; // if the parameters of the method are not ok, the method is bot fine
					res+=paramResults;
					exists=true;
				}
			if(!exists){
				res+="The method "+mm.getName()+" does not exist in the diagram class \n";
				found_wrong_method=true;
			}
		}
		if(!(found_wrong_method))
			methods_are_fine=true; // no method has been found in diagram or bytecode class with irregularities 
		return res;
	}
	public String checkParameters(Method m, Operation op)
	{
		/*
		 * this method checks the parameters of a bytecode method and a diagram operation
		 */
		String result="";
		List<ParamPair> dp=op.getParams();
		Parameter[] pm = m.getParameters();
		int diagramParams=dp.size();
		if (hasReturnParam(op))
			diagramParams-=1; // the number of parameters does not count the return one
		if(m.getParameterCount()!=diagramParams) //check the number of parameters
			result+="The methods "+m.getName()+" from diagram and class file have different numbers of parameters\n";
		for (Parameter pp:pm){
			result+=checkParamType(pp,dp);
		}
			result+=checkParamReturn(m,dp);
		return result;
	}
	public boolean hasReturnParam(Operation o)
	{
		/*
		 * if an operation has a return parameter, this method return a positive result.
		 */
		for(ParamPair pp:o.getParams())
			if(pp.getDirection().equals("return"))
				return true;
		return false;
	}
	public MyClass getDiagramClassByName(String name)
	{
		/*
		 * Searches the list of diagram classes by name
		 */
		for(MyClass dcl:diagramclasses)
			if(name.equals(dcl.getName()))
				return dcl;
		return null;
	}
	public String checkParamType(Parameter pp, List<ParamPair> dp)
	{
		/*
		 * this method checks if the types of bytecode and diagram parameters match
		 */
		String res ="";
		res+="The parameter " + pp.getName()+"of type "+pp.getType()+" does not have the same type as in the diagram.\n";
		Class<?> ty ;
		for(ParamPair pair:dp)
		{
			ty= pp.getType();
			String actualtype=getActualType(ty);
			if(actualtype.equals(pair.getRealType()))
				res="";
		}
		return res;
	}
	public String checkParamReturn(Method m,List<ParamPair> dp)
	{
		/*
		 * this method checks if the return types of the parameters from bytecode method and from diagram are the same
		 */
		String res="";
		res="The return type for the method is not like in the diagram.\n";
		String actualtype=getActualType(m.getReturnType());
		boolean foundReturn=false;
		for(ParamPair pp : dp)
			if(pp.getDirection().equals("return")){
				if(actualtype.equals(pp.getRealType()))
					res="";
				foundReturn=true;
			}
		//if the diagram class operation doesn't have a return type and the bytecode class mathod returns void, then they match
		if(foundReturn==false)
			if(actualtype.equals("void"))
				res="";
		return res;
	}
	public String getActualType(Class <?> cc)
	{
		/*
		 * This method helps determine the type of a bytecode object
		 * For ease of comparison, instead of returning the full name of the type, it returns simple class names
		 * This makes it easier to compare bytecode and diagram types 
		 * Input: A Class cc
		 * Output: A string representing the type of the Class cc
		 */
		String actualtype="";
		//System.out.println(cc.getName()+"<-Type");
		if(cc.getName().contains("String"))
			actualtype="String";
		if(cc.getName().contains("Integer") || cc.getName().contains("int"))
			actualtype="int";
		if(cc.getName().contains("Float") || cc.getName().contains("float") || cc.getName().contains("Real"))
			actualtype="float";
		if(cc.getName().contains("boolean") || cc.getName().contains("Boolean"))
			actualtype="boolean";
		if(cc.getName().contains("Void")|| cc.getName().contains("null") ||  cc.getName().contains("Null") ||  cc.getName().contains("void"))
			actualtype="void";
		if(cc.getName().contains("List"))
			actualtype="List<T>";
		if(cc.getName().contains("Map"))
			actualtype="Map<Key,Value>";
		if(cc.getName().contains("Set"))
			actualtype="Set<T>";
		return actualtype;
	}
}