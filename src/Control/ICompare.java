package Control;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import Model.MyClass;
import Model.Operation;
import Model.ParamPair;

public interface ICompare {

	public String compare();
	public void patch(int i);
	public String checkClass(Class c);
	public String checkfields(Field[] f,MyClass dcl);
	public String checkFieldName(Field ff,MyClass dcl);
	public String checkFieldType(Field ff, MyClass dcl);
	public String chackFieldVisibility(Field ff,MyClass dcl);
	public String checkContructors(Constructor[] c, MyClass dcl);
	public int getNumberofConstructorsInDiagramClass(MyClass dcl);
	public String checkmethods(Method[] m,MyClass dcl);
	public String checkParameters(Method m, Operation op);
	public boolean hasReturnParam(Operation o);
	public MyClass getDiagramClassByName(String name);
	public String checkParamType(Parameter pp, List<ParamPair> dp);
	public String checkParamReturn(Method m,List<ParamPair> dp);
	public String getActualType(Class <?> cc);
}
