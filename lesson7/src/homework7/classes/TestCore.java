package homework7.classes;

import homework7.anotations.AfterSuite;
import homework7.anotations.BeforeSuite;
import homework7.anotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TestCore {

    public static void start(final Class c) {
        LinkedList<Method> allMethods = new LinkedList<>(Arrays.asList(c.getMethods()));
        LinkedList<Method> annotatedMethods = getAnnotatedMethodsForTests(allMethods);

        for (Method annotatedMethod : annotatedMethods) {
            try {
                annotatedMethod.invoke(c);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static LinkedList<Method> getAnnotatedMethodsForTests(LinkedList<Method> allMethods) {
        LinkedList<Method> annotatedMethods = new LinkedList<>();
        Method beforeSuiteMethod = null, afterSuiteMethod = null;

        for (Method method : allMethods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuiteMethod == null) beforeSuiteMethod = method;
                else throw new RuntimeException("Too many BeforeSuite annotated methods!");
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuiteMethod == null) afterSuiteMethod = method;
                else throw new RuntimeException("Too many AfterSuite annotated methods!");
            } else if (method.isAnnotationPresent(Test.class)) {
                Test testAnnotation = method.getAnnotation(Test.class);

                if (testAnnotation.priority() >= 1 && testAnnotation.priority() <= 10)
                    annotatedMethods.add(method);
                else throw new RuntimeException("Illegal priority at Test annotation!");
            }
        }

        annotatedMethods = annotatedMethods.stream()
                .sorted((o1, o2) -> TestComparator.compare(o2.getAnnotation(Test.class),
                        o1.getAnnotation(Test.class)))
                .collect(Collectors.toCollection(LinkedList::new));

        if (beforeSuiteMethod != null) annotatedMethods.addFirst(beforeSuiteMethod);
        if (afterSuiteMethod != null) annotatedMethods.addLast(afterSuiteMethod);

        return annotatedMethods;
    }
}
