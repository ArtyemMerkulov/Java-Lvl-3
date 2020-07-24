package homework1;

public abstract class Fruit {

    private String type;
    private float weight;

    public Fruit(String type, float weight) {
        this.type = type;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void info() {
        System.out.println(type + " weighing " + weight + " units");
    }
}
