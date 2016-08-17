package Services;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import Model.*;
import Control.IXMLParser;
public class XMLParser implements IXMLParser
{
	private File file;
	private NodeList opList;
	private NodeList atList;
	private NodeList reList;
	private NodeList genList;
	private NodeList nList;
	private List<MyClass> loadedClasses=new LinkedList<MyClass>();
	
	public XMLParser(File f)
	{
		file = f;
	}
	public List<MyClass> getClasses()
	{
		return loadedClasses;
	}
  public void loadXmlFile() {

    try {

	
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(file);
	
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
	nList = doc.getElementsByTagName("packagedElement");
			
	System.out.println("----------------------------");

	for (int temp = 0; temp < nList.getLength(); temp++) {

		Node nNode = nList.item(temp);
		/*
		 * Get the classes and the interfaces from the diagram
		 */
		System.out.println("\nCurrent Element :" + nNode.getNodeName());
		Element ee = (Element) nNode;
		if(ee.getAttribute("xmi:type").equals("uml:Class") || ee.getAttribute("xmi:type").equals("uml:Interface") )
		{
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	
				//Element eElement = (Element) nNode;
				String name;
				String id;
				
				System.out.println("Packaged Element Name : " + ee.getAttribute("name"));
				name = ee.getAttribute("name");
				id=ee.getAttribute("xmi:id");
				//System.out.println("\n**********\n"+id+" funtsita getID\n****\n");
				//System.out.println("Class : " + eElement.getElementsByTagName("packagedElement").item(0).getTextContent());
				opList = ee.getElementsByTagName("ownedOperation"); // get the operations of a diagram class
				getOperations();
				atList = ee.getElementsByTagName("ownedAttribute"); // get the attributes of a diagram class
				getAttributes();
				//create a MyClass object, using the list of operations, the list of attributes and the name and id of the diagram class
				MyClass cl = new MyClass(getOperations(),getAttributes(),name,id); 
				if(ee.getAttribute("xmi:type").equals("uml:Interface")) // check if the diagram class is an interface
					cl.setInterface(true);
				reList = ee.getElementsByTagName("interfaceRealization"); // check if it realizes any interfaces
				for (int temp1 = 0; temp1 < reList.getLength(); temp1++) {
					  Node n = reList.item(temp1);
					  Element eee = (Element) n;
					  String supplier = eee.getAttribute("supplier");
					  cl.addRealisation(supplier);
				}
				genList = ee.getElementsByTagName("generalization"); // check if the diagram class is a subclass of another
				for (int temp1 = 0; temp1 < genList.getLength(); temp1++) {
					  Node n = genList.item(temp1);
					  Element eee = (Element) n;
					  String general = eee.getAttribute("general");
					  cl.addGeneral(general);
				}
				loadedClasses.add(cl);
			}
		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
  }
  public  List<Operation> getOperations(){
	  /*
	   * this method loads all the registered operations of a diagram class
	   */
	  List<Operation> operations = new LinkedList<Operation>();
	  for (int temp = 0; temp < opList.getLength(); temp++) {
		  Node n = opList.item(temp);
		  Element eee = (Element) n;
		  String name = eee.getAttribute("name"); // get the name of the operation
		  String id = eee.getAttribute("xmi:id"); // get the id
		  String visibility="public";
		  List<ParamPair> params=new LinkedList<ParamPair>(); // get the visibility of an operation
		  try{
			  if(eee.getAttribute("visibility").equals(""))
				  visibility="public";
			  else 
				  visibility=eee.getAttribute("visibility");
		  }
		  catch(Exception e)
		  {
			  //visibility="public";
		  }
		  try{
			  NodeList tt = eee.getElementsByTagName("ownedParameter"); // get the parameter list of the operation
			  for (int temp1 = 0; temp1 < tt.getLength(); temp1++){
				  String type="";
				  String direction="in";
				  int multiplicity=1;
				  String mul1 = "1" , mul2 = "1";
				  String param_name="";
				  Node nn = tt.item(temp1);
				  Element e3= (Element) nn;
				  param_name=e3.getAttribute("name"); // get the name of the parameter
				  try{
					  if(e3.getAttribute("direction").equals("")) // get the direction of a parameter
						  direction="in";
					  else 
						  direction=e3.getAttribute("direction");
				  }
				  catch(Exception e)
				  {
					  //direction="in";
				  }
				  try{
					  type=e3.getAttribute("type");
				  }
				  catch(Exception e)
				  {
					  
				  }
				  
				  try{
					  NodeList t3 = e3.getElementsByTagName("type");
					  for (int temp11 = 0; temp11 < t3.getLength(); temp11++){
						  Node nn1 = t3.item(temp11);
						  Element e31= (Element) nn1;
						  type= e31.getAttribute("href").split("#")[1]; // get the type of an parameter
					  }
					  // get the multiplicity of a parameter
					  NodeList lower  = e3.getElementsByTagName("lowerValue");
					  for (int temp11 = 0; temp11 < lower.getLength(); temp11++){
						  Node nn1 = lower.item(temp11);
						  Element e31= (Element) nn1;
						  mul1=e31.getAttribute("value");
					  }
					  NodeList upper  = e3.getElementsByTagName("upperValue");
					  for (int temp11 = 0; temp11 < upper.getLength(); temp11++){
						  Node nn1 = upper.item(temp11);
						  Element e31= (Element) nn1;
						  mul2=e31.getAttribute("value");
					  }
					  if(mul1.equals("1") && mul2.equals("1"))
						  multiplicity=1; // there is only one object
					  if(mul2.equals("*")) 
						  multiplicity=2; // the parameter is a list of more objects
				  }
				  catch(Exception e)
				  {
					  
				  }
				  ParamPair ppp=new ParamPair(type, direction);
				  ppp.setName(param_name);
				  ppp.setMultiplicity(multiplicity);
				  params.add(ppp);
			  }
		  }
		  catch(Exception e)
		  {
			  
		  }
		  Operation op= new Operation(name, params, visibility);
		  operations.add(op);
	  }
	  return operations;
	  
  }
  public List<Attribute> getAttributes()
  {
	  /*
	   * this method compiles the list of all the attributes of a diagram class
	   */
	  List<Attribute> attributes = new LinkedList<Attribute>();
	  for (int temp = 0; temp < atList.getLength(); temp++) {
		  Node n = atList.item(temp);
		  Element eee = (Element) n;
		  String name = eee.getAttribute("name"); // get the name of the attribute
		  String id = eee.getAttribute("xmi:id"); //get the id
		  int multiplicity=1;
		  String mul1 = "1" , mul2 = "1";
		  String visibility=eee.getAttribute("visibility"); //get visibility
		  String type="";
		  try{
			  type=eee.getAttribute("type"); // get the type, if is posiible using the 'type' attribute in xml
		  }
		  catch(Exception e)
		  {
			  
		  }
		  try{
			  NodeList tt = eee.getElementsByTagName("type"); // get the type using the 'type' tag name
			  for (int temp1 = 0; temp1 < tt.getLength(); temp1++){
				  Node nn = tt.item(temp1);
				  Element e3= (Element) nn;
				  type= e3.getAttribute("href").split("#")[1];
			  }
			  // get multiplicity
			  NodeList lower  = eee.getElementsByTagName("lowerValue");
			  for (int temp1 = 0; temp1 < lower.getLength(); temp1++){
				  Node nn = lower.item(temp1);
				  Element e3= (Element) nn;
				  mul1=e3.getAttribute("value");
			  }
			  NodeList upper  = eee.getElementsByTagName("upperValue");
			  for (int temp1 = 0; temp1 < upper.getLength(); temp1++){
				  Node nn = upper.item(temp1);
				  Element e3= (Element) nn;
				  mul2=e3.getAttribute("value");
			  }
			  if(mul1.equals("1")&&mul2.equals("1"))
				  multiplicity=1;
			  if(mul2.equals("*"))
				  multiplicity=2;
		  }
		  catch(Exception e)
		  {
			  
		  }
		  Attribute at=new Attribute(name,id,type,visibility);
		  at.setMultiplicity(multiplicity);
		  attributes.add(at);
	  }  
	  return attributes;
  }

}