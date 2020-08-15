package homework7.classes;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class HomeworkChecker {

    private File homeworkClassesDir;

    public HomeworkChecker() {
        this.homeworkClassesDir = new File(System.getProperty("user.dir"));
    }

    public HomeworkChecker(String homeworkClassesDirPath) {
        this.homeworkClassesDir = new File(homeworkClassesDirPath);
    }

    public void setHomeworkClassesDir(String homeworkClassesDirPath) {
        this.homeworkClassesDir = new File(homeworkClassesDirPath);
    }

    public void startCheck(Object[] answers, Object[][] params) throws Exception {
        if (answers.length != params.length)
            throw new IllegalArgumentException("Invalid arguments.");

        ArrayList<String> fileNames = getFilesNames();

        int currMethodIdx = 0;

        for (String fileName : fileNames) {
            Class c = URLClassLoader.newInstance(new URL[]{homeworkClassesDir.toURL()}).loadClass(fileName);
            Constructor constructor = c.getConstructor();
            Object object = constructor.newInstance();
            Method[] methods = Arrays.stream(c.getDeclaredMethods())
                    .filter(method -> !(isObjectMethod(method) || hasPrimitiveType(method)))
                    .sorted(Comparator.comparing(Method::getName))
                    .toArray(Method[]::new);

            for (Method method : methods) {
                Object result = method.invoke(object, params[currMethodIdx]);

                if (result.equals(answers[currMethodIdx]))
                    System.out.println("Task " + method.getName() + " completed!!!");
                else System.out.println("Task " + method.getName() + " not completed!!!");

                currMethodIdx++;
            }
        }
    }

    private ArrayList<String> getFilesNames() {
        String[] fileList = homeworkClassesDir.list();

        if (fileList == null)
            throw new RuntimeException(homeworkClassesDir.getAbsolutePath() + " is empty.");

        ArrayList<String> fileNames = new ArrayList<>();

        for (String file : fileList)
            if (file.length() >= 7) {
                String fileExt = file.substring(file.length() - 6);

                if (fileExt.equalsIgnoreCase(".class"))
                    fileNames.add(file.substring(0, file.length() - 6));
            }

        if (fileNames.size() == 0)
            throw new RuntimeException(homeworkClassesDir.getAbsolutePath() + " has no .class files.");

        return fileNames;
    }

    private boolean isObjectMethod(Method method) {
        String methodName = method.getName();
        return  methodName.equals("toString")   ||  methodName.equals("wait")   ||  methodName.equals("hashCode")   ||
                methodName.equals("getClass")   ||  methodName.equals("notify") ||  methodName.equals("notifyAll");
    }

    private boolean hasPrimitiveType(Method method) {
        Class methodReturnType = method.getReturnType();
        return  methodReturnType.equals(void.class)     ||  methodReturnType.equals(byte.class)     ||
                methodReturnType.equals(short.class)    ||  methodReturnType.equals(int.class)      ||
                methodReturnType.equals(long.class)     ||  methodReturnType.equals(float.class)    ||
                methodReturnType.equals(double.class)   ||  methodReturnType.equals(char.class)     ||
                methodReturnType.equals(boolean.class);
    }
}
