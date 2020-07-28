package homework2;

public class Triplet<F, S, T> {
    private F first;
    private S second;
    private T third;

    public Triplet() {
        this.first = null;
        this.second = null;
        this.third = null;
    }

    public Triplet(F first, S second, T third) {
       this.first = first;
       this.second = second;
       this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    public void updateFirst(F first) {
        this.first = first;
    }

    public void updateSecond(S second) {
        this.second = second;
    }

    public void updateThird(T third) {
        this.third = third;
    }
}
