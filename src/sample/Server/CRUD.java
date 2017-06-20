package sample.Server;

import sample.Annotation.Column;
import sample.Annotation.Entity;
import sample.Entity.Potatoes;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vadim on 19.06.2017.
 */
public interface CRUD {
    void putData(LinkedList<Potatoes> gettingPotatoes);
    LinkedList<Potatoes> getData();
    void deleteData(LinkedList<Potatoes> gettingPotatoes);

    default void initTable (Connection connection, Class neededClass){
        Entity entity = (Entity) neededClass.getAnnotation(Entity.class);
        List<Column> columns=new LinkedList<>();
        Arrays
                .stream(neededClass.getDeclaredFields())
                .forEach(field -> columns.add(field.getAnnotation(Column.class)));
        columns.remove(0);
        StringBuilder createTable = new StringBuilder("CREATE TABLE ");
        createTable.append(entity.tableName()).append("(\n");
        columns.forEach(column ->
                    createTable.append("    ")
                            .append(column.attributeName())
                            .append(" ")
                            .append(column.type())
                            .append(",\n")
                );
        createTable.replace(createTable.length()-2,createTable.length()-1,"");
        createTable.append(");");
        try(Statement statement = connection.createStatement()){
            statement.execute(String.valueOf(createTable));
            statement.close();
        }catch (SQLException e){
            System.out.println("Таблица есть, можно работать...");
        }
    }

}
