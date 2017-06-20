package sample.Entity;


import java.time.ZonedDateTime;

public class Potatoes extends Vegetables implements Comparable{
    private static final long serialVersionUID = 10;

    public Potatoes(int id, String color, int weight, ZonedDateTime zonedDateTime) {
        super(id, color, weight, zonedDateTime);
    }

    public Potatoes(String color, int weight, ZonedDateTime zonedDateTime) {
        super(color, weight, zonedDateTime);
    }

    public Potatoes(String color, int weight) {
        super(color, weight);
    }

    public Potatoes(int weight) {
        super(weight);
    }

    public Potatoes() {
        super(1);
    }

    @Override
    public int compareTo(Object o) {
        return this.getWeight()-((Potatoes)o).getWeight();
    }
}
