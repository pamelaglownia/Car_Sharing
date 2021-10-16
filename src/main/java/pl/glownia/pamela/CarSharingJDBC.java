package pl.glownia.pamela;

import java.sql.*;

class CarSharingJDBC {
    // JDBC driver name and database URL

    private Connection connection;
    private String DB_URL = "jdbc:h2:file:./src/main/java/pl/glownia/pamela/db/";


    CarSharingJDBC(String dataBaseFileName) {
        final String JDBC_DRIVER = "org.h2.Driver";
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
}