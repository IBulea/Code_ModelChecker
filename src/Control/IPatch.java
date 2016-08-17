package Control;

import java.io.IOException;
import java.util.List;

import Model.MyClass;

public interface IPatch {

	public void startPatching(int i) throws IOException;
	public void add(MyClass c);
	public void SetAllDiagramClasses(List<MyClass> cl_list);
}
