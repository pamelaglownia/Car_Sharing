package pl.glownia.pamela;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class CarSharingJDBC implements CompanyDao {
    // JDBC driver name and database URL

    private final List<Company> companies;
    private Connection connection;

    CarSharingJDBC(String dataBaseFileName) {
        companies = new ArrayList<>();
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:file:./src/main/java/pl/glownia/pamela/db/";
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
    }

    void createTable() {
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

    @Override
    public void insertRecordToTable(String companyName) {
        try {
            Statement statement = connection.createStatement();
            String recordToInsert = "INSERT INTO COMPANY (name)" +
                    "VALUES('" + companyName + "')";
            statement.executeUpdate(recordToInsert);
            System.out.println("The company was created!");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Company> readRecords() {
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT id, name FROM COMPANY";
            ResultSet resultSet = statement.executeQuery(recordToRead);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                companies.add(new Company(id, name));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return companies;
    }

    @Override
    public void getAllCompanies() {
        List<Company> companies = readRecords();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Company list:");
            companies.forEach(System.out::println);
        }
    }
}