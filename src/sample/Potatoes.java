package sample;


import java.io.Serializable;

public class Potatoes implements Serializable {
    static final long serialVersionUID = 10L;
    private double weight = 1;
    private String color = "White";
    private int id;
    public Potatoes(int id, String color, int weight){
        this.id=id;
        this.color=color;
        this.weight=weight;
    }
    public Potatoes(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    public Potatoes(double weight) {
        this.weight = weight;
    }

    public Potatoes() {
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

    public int hashCode(){
        return (int)this.weight+this.color.length()+this.id;
    }
    public int getId(){
        return id;
    }

}
