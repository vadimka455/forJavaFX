package sample.Entity;

import com.sun.istack.internal.NotNull;
import sample.Annotation.Column;
import sample.Annotation.Entity;
import sample.Annotation.PrimaryKey;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by vadim on 19.06.2017.
 */
@Entity(tableName = "potatoe")
public abstract class Vegetables implements Serializable, Comparable {
    private static final long serialVersionUID = 10;

    @Column(fieldName = "id", attributeName = "id",type = "SERIAL PRIMARY KEY")
    @PrimaryKey(columnName = "id")
    private int id;
    @Column(fieldName = "color", attributeName = "color",type = "TEXT NOT NULL")
    private String color;
    @Column(fieldName = "weight", attributeName = "weight",type = "INTEGER")
    private int weight;
    @Column(fieldName = "zonedDateTime", attributeName = "date", type="TEXT")
    private String zonedDateTime;

    public Vegetables(int id, String color, int weight, ZonedDateTime zonedDateTime) {
        this.id = id;
        this.color = color;
        this.weight = weight;
        this.zonedDateTime = zonedDateTime.toString();
    }

    public Vegetables(String color, int weight, ZonedDateTime zonedDateTime) {
        this.color = color;
        this.weight = weight;
        this.zonedDateTime = zonedDateTime.toString();
    }

    public Vegetables(String color, int weight) {
        this.color = color;
        this.weight = weight;
        this.zonedDateTime=ZonedDateTime.now().toString();
    }

    public Vegetables(int weight) {
        this.weight = weight;
        this.color="White";
    }

    public Vegetables() {
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ZonedDateTime getZonedDateTime() {
//        return (zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm dd MMMM yyyy ", Main.locale)));
        return ZonedDateTime.parse(zonedDateTime);
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime.toString();
    }

    @Override
    public int compareTo( @NotNull Object o) {
        return this.getWeight()-((Vegetables)o).getWeight();
    }

    @Override
    public int hashCode() {
        return color.length()+weight+id;
    }
}
