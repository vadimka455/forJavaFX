package sample.Server;

import sample.Potatoes;

import java.sql.*;
import java.util.LinkedList;

/**
 * Created by vadim on 05.06.2017.
 */
public class ServerDB {
    final private String URLDB ="jdbc:postgresql://localhost:5432/postgres";
    final private String USER ="postgres";
    final private String PASSWORD="postgres";
    {

        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URLDB, USER, PASSWORD);
    }
    void putData(LinkedList<Potatoes> gettingPotatoes){
        try {
            Connection connection = connect();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("insert into potatoe(color,weight) values(?,?)");

            for (Potatoes potatoes :gettingPotatoes){
                ps.setString(1,potatoes.getColor());
                ps.setDouble(2,potatoes.getWeight());
                ps.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void deleteData(LinkedList<Potatoes> gettingPotatoes){
        String sqlRequest ="DELETE FROM potatoe WHERE id = ?";
        try {
            Connection connection = connect();
            PreparedStatement ps = connection.prepareStatement(sqlRequest);
            connection.setAutoCommit(false);
            for (Potatoes potatoes: gettingPotatoes){
                ps.setInt(1,potatoes.getId());
                ps.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    LinkedList<Potatoes> getData(){
        String sqlRequest = "SELECT * FROM potatoe ORDER BY weight,id ASC";
        LinkedList<Potatoes> listOfPotatoes= new LinkedList<>();
        try {
            Connection connection =connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlRequest);
            while (resultSet.next()){
                listOfPotatoes.add(
                        new Potatoes(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getInt(3)));
            }
            statement.close();
            resultSet.close();
            return listOfPotatoes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfPotatoes;
    }
}
