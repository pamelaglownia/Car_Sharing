package pl.glownia.pamela;

import java.sql.*;

class CarSharingJDBC {

    private Connection connection;
    private String DB_URL = "jdbc:h2:file:./src/main/java/pl/glownia/pamela/db/";
    final String JDBC_DRIVER = "org.h2.Driver";

    private String getUrl(String dataBaseFileName) {
        return DB_URL + dataBaseFileName;
    }

    Connection getConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    void createDataBase(String dataBaseFileName) {
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Open a connection
            DB_URL = getUrl(dataBaseFileName);
            getConnection();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

}