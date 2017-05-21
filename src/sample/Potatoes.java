package sample;


public class Potatoes {
    private double weight = 1;
    private String color = "White";
    Potatoes(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    Potatoes(double weight) {
        this.weight = weight;
    }

    Potatoes() {
        //
    }

    public double getWeight() {
        return this.weight;
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return this.color;
    }

    void setColor(String color) {
        this.color = color;
    }

}
