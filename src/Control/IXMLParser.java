package Control;

import java.util.List;

import Model.Attribute;
import Model.MyClass;
import Model.Operation;

public interface IXMLParser {

	public void loadXmlFile();
	public  List<Operation> getOperations();
	public List<Attribute> getAttributes();
	public List<MyClass> getClasses();
}
