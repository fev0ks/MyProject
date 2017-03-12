package scienceWork.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorDB {



    private Connection connection;

     ConnectorDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
               String URL = "jdbc:mysql://localhost:3306/scientific_work";
               String USERNAME = "root";
               String PASSWORD = "root";
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    Connection getConnection() {
        return connection;
    }


}
