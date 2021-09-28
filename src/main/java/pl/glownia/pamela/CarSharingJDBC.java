package pl.glownia.pamela;

import java.sql.*;

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
                    "NAME VARCHAR UNIQUE NOT NULL)";
            statement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void insertRecordToTable(Connection connection, String name) {
        try {
            Statement statement = connection.createStatement();
            String recordToInsert = "INSERT INTO COMPANY (name)" +
                    "VALUES('" + name + "')";
            statement.executeUpdate(recordToInsert);
            System.out.println("The company was created!");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void readRecords(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT id, name FROM COMPANY";
            ResultSet resultSet = statement.executeQuery(recordToRead);
            if (!resultSet.next()) {
                System.out.println("The company list is empty!");
            } else {
                System.out.println("Company list:");
                do {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    System.out.print(id + ". " + name + "\n");
                } while (resultSet.next());
                resultSet.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}