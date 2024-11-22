package AspireAI.CustomFactories;

public class CustomData {

	public static String className;
	public static String elementName;
	public static ThreadLocal<Boolean> flag = new ThreadLocal<Boolean>();
	public static ThreadLocal<Boolean> aiflag = new ThreadLocal<Boolean>();
	public static String pageclassName;

	CustomData(){
//		pageclassName = null;
		className = null;
		elementName = null;
		flag.set(true);
		aiflag.set(true);
	}

}
