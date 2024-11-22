package ICar.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * RetryListener Class to retry Listener
 */

@SuppressWarnings("rawtypes")
public class RetryListener implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,
            Method testMethod) {
        testannotation.setRetryAnalyzer(RetryAnalyzer.class);

    }
}