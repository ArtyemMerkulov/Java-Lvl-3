package homework1;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {

    private ArrayList<T> box;

    public Box() {
        box = new ArrayList<>(0);
    }

    public Box(Box box) {
        shiftTo(box);
    }

    public void add(T e) {
        box.add(e);
    }

    public T get(int pos) {
        return box.get(pos);
    }

    public List<T> getElements(int beginPos, int n) {
        return box.subList(beginPos, beginPos + n);
    }

    public List<T> getAllElements() {
        return getElements(0, box.size());
    }

    public float getWeight() {
        return box.stream().mapToInt(e -> e.getWeight()).sum();
    }

    public int getTotalElements() {
        return box.size();
    }

    public T removeElements(int index) {
        return box.remove(index);
    }

    public void clear() {
        box.clear();
        box.ensureCapacity(0);
    }

    public void shiftTo(Box box) {
        if (this == box) return;

        for (T e : this.box) box.add(e);
        this.clear();
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) return true;

        if (anObject instanceof Box)
            return this.getWeight() == ((Box) anObject).getWeight();

        return false;
    }
}
