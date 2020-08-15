package homework7.classes;

import homework7.anotations.Test;

public class TestComparator {
    public static int compare(Test o1, Test o2) {
        if (o1.priority() > o2.priority()) return 1;
        else if (o1.priority() == o2.priority()) return 0;
        else return -1;
    }
}
