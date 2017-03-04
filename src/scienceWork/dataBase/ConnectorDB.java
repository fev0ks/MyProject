package scienceWork.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorDB {

    private static final String URL = "jdbc:mysql://localhost:3306/scientific_work";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection;

    public ConnectorDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  Connection getConnection() {
        return connection;
    }


}
