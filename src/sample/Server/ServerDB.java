package sample.Server;

import sample.Annotation.Column;
import sample.Annotation.Entity;
import sample.Annotation.PrimaryKey;
import sample.Entity.Potatoes;
import sample.Entity.Vegetables;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by vadim on 05.06.2017.
 */
public class ServerDB implements CRUD {
    private Properties properties = new Properties();
    final private String USER = "postgres";
    final private String PASSWORD = "postgres";

    {
        properties.put("user","postgres");
        properties.put("password","postgres");
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Connection connect() throws SQLException {
        String URLDB = "jdbc:postgresql://localhost:5432/postgres";
        return DriverManager.getConnection(URLDB, properties);
    }

    private void executeQuery(String query){

        try {
            Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void putData(LinkedList<Potatoes> gettingPotatoes) {
        for (Potatoes potatoes :
                gettingPotatoes) {
            Entity entity = potatoes.getClass().getSuperclass().getAnnotation(Entity.class);

            LinkedList<Field> columns = new LinkedList<>();
            Arrays.stream(potatoes.getClass().getSuperclass().getDeclaredFields())
                    .filter(field -> field.getAnnotation(Column.class) != null)
                    .filter(field -> field.getAnnotation(PrimaryKey.class) == null)
                    .forEach(columns::add);


            List<Column> annotationColumns = new LinkedList<>();
            Arrays.stream(potatoes.getClass().getSuperclass().getDeclaredFields())
                    .filter(field -> field.getAnnotation(Column.class) != null)
                    .filter(field -> field.getAnnotation(PrimaryKey.class) == null)
                    .forEach(field -> annotationColumns.add(field.getAnnotation(Column.class)));


            StringBuilder query = new StringBuilder("INSERT INTO ");
            query.append(entity.tableName());
            query.append(" (");
            annotationColumns
                    .forEach(column -> {
                        query.append(column.attributeName());
                        query.append(", ");
                    });

            query.replace(query.length() - 2, query.length(), "");
            query.append(")");

            query.append("\n VALUES (");

            columns
                    .forEach(field -> {
                        Arrays.stream(potatoes.getClass().getSuperclass()
                                .getMethods())
                                .filter(method -> (method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
                                .filter(method -> method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
                                .forEach(method -> invokeMethod(method,potatoes,query));
                        query.append(", ");
                    });

            query.replace(query.length() - 2, query.length(), "");
            query.append(");");
            executeQuery(query.toString());
        }
    }


    private void invokeMethod(Method method, Potatoes potatoes, StringBuilder query) {
        try {
            boolean flag = method.getReturnType() == String.class || method.getReturnType() == ZonedDateTime.class;
            if (flag) {
                query.append(" '")
                        .append(method.invoke(potatoes))
                        .append("' ");
            }else{
                query.append(method.invoke(potatoes));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(LinkedList<Potatoes> gettingPotatoes) {
        for (Potatoes potatoes :
                gettingPotatoes) {
            Entity entity = potatoes.getClass().getSuperclass().getAnnotation(Entity.class);
            LinkedList<Field> primaryKeys=new LinkedList<>();
            Arrays.stream(potatoes.getClass().getSuperclass().getDeclaredFields())
                    .filter(field -> field.getAnnotation(PrimaryKey.class)!=null)
                    .forEach(primaryKeys::add);

            StringBuilder query = new StringBuilder("DELETE FROM ");
            query.append(entity.tableName())
                    .append("\n WHERE ( ");
            primaryKeys
                    .forEach(field -> {
                        query.append(" ")
                                .append(field.getName())
                                .append(" = ");
                        Arrays.stream(potatoes.getClass().getSuperclass()
                        .getMethods())
                                .filter(method -> (method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
                                .filter(method -> method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
                                .forEach(method -> {
                                    try {
                                        query.append(method.invoke(potatoes));
                                    } catch (IllegalAccessException | InvocationTargetException e1) {
                                        e1.printStackTrace();
                                    }
                                });
                        query.append(", ");
                    });
            query.replace(query.length() - 2, query.length() - 1, "");
            query.append(");");
            executeQuery(query.toString());
        }
    }

    public LinkedList<Potatoes> getData() {
        Entity entity = Vegetables.class.getAnnotation(Entity.class);
        String sqlRequest = "SELECT * FROM "+
                entity.tableName();
        LinkedList<Potatoes> listOfPotatoes = new LinkedList<>();
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlRequest);
            while (resultSet.next()) {
                listOfPotatoes.add(
                        new Potatoes(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getInt(3),
                                ZonedDateTime.parse((resultSet.getString(4)))))
                ;
            }
            statement.close();
            resultSet.close();
            connection.close();
            return listOfPotatoes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfPotatoes;
    }
}
