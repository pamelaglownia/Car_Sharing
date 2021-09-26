package pl.glownia.pamela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class CarSharingJDBC {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:file:./src/main/java/pl/glownia/pamela/db/";

    Connection getConnection(String dataBaseFileName) {
        Connection connection = null;
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Open a connection
            connection = DriverManager.getConnection(DB_URL + dataBaseFileName);
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    void createTable(Connection connection) {
        try {
            //Execute a query
            Statement statement = connection.createStatement();
            String table = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR NOT NULL)";
            statement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void insertRecordToTable(Connection connection, int id, String name) {
        try {
            Statement statement = connection.createStatement();
            String recordToInsert = "INSERT INTO COMPANY " +
                    "VALUES(" + id + ",'" + name + "')";
            statement.executeUpdate(recordToInsert);
            System.out.println("Inserted records into table...");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
