package homework1;

public abstract class Fruit {

    private String type;
    private int weight;

    public Fruit(String type, int weight) {
        this.type = type;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void info() {
        System.out.println(type + " weighing " + weight + " units");
    }
}
