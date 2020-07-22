package homework1;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {

    private ArrayList<T> box;
    private float weight;

    public Box() {
        box = new ArrayList<>(0);
        weight = 0;
    }

    public void shiftTo(Box box) {
        if (this == box) return;

        for (T e : this.box) box.add(e);
        clear();
    }

    public void add(T e) {
        box.add(e);
        weight += e.getWeight();
    }

    public void clear() {
        box.clear();
        box.ensureCapacity(0);
        weight = 0;
    }

    public boolean isWeightEquals(Box box) {
        return getWeight() == box.getWeight();
    }

    public T get(int pos) {
        return box.get(pos);
    }

    public float getWeight() {
        return weight;
    }

    public int getTotalElements() {
        return box.size();
    }

    public List<T> getElements(int beginPos, int n) {
        return box.subList(beginPos, beginPos + n);
    }

    public List<T> getAllElements() {
        return getElements(0, box.size());
    }

    public T remove(int index) {
        return box.remove(index);
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) return true;

        if (anObject instanceof Box)
            return getWeight() == ((Box) anObject).getWeight();

        return false;
    }
}
