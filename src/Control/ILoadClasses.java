package Control;

import java.io.File;
import java.util.List;

public interface ILoadClasses {

	public void load();
	public List<Class> getClasses();
	public void addtonames(String a);
	public File addtourls(String string);
}
