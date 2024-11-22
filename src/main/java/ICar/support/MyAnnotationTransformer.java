package ICar.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * RetryListener Class to retry Listener
 */



public class MyAnnotationTransformer implements IAnnotationTransformer {
	
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	@Override
	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		try {
			String methodToRun = System.getProperty("methodToRun")!=null ? System.getProperty("methodToRun") : configProperty.getProperty("methodToRun");
			if(!methodToRun.isEmpty() && !methodToRun.trim().equals(testMethod.getName())) {
				testannotation.setEnabled(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}