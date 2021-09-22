package pl.glownia.pamela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class CarSharingJDBC {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:file:./src/main/java/pl/glownia/pamela/db/";

    private void createDataBase(String dataBase) {
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //Open a connection
            Connection connection = DriverManager.getConnection(DB_URL + dataBase);

            //Execute a query
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INTEGER, " +
                    "NAME VARCHAR)";
            statement.executeUpdate(sql);

            // STEP 4: Clean-up environment
            statement.close();
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    void runDataBase() {
        Input input = new Input();
        String[] arrayWithDataBaseFileName = input.takeDataBaseName();
        if (arrayWithDataBaseFileName.length > 1 && arrayWithDataBaseFileName[0].equals("-databaseFileName")) {
            createDataBase(arrayWithDataBaseFileName[1]);
        }
    }
}
