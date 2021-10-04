package pl.glownia.pamela;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

class CompanyTable implements CompanyDao {
    private Connection connection;
    private List<Company> companies;

    public CompanyTable(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public void createTable(CarSharingJDBC dataBase) {
        connection = dataBase.getConnection();
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
    public void addNewCompany() {
        System.out.println("Enter the company name:");
        Input input = new Input();
        String companyName = input.getNewItem();
        insertRecordToTable(companyName);
        System.out.println();
    }

    @Override
    public void insertRecordToTable(String companyName) {
        try {
            Statement statement = connection.createStatement();
            String recordToInsert = "INSERT INTO COMPANY (NAME)" +
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
            String recordToRead = "SELECT ID, NAME FROM COMPANY";
            ResultSet resultSet = statement.executeQuery(recordToRead);
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                companies.add(new Company(id, name));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return companies;
    }

    @Override
    public void getAll() {
        if (isEmptyList()) {
            System.out.println("The company list is empty!");
        } else {
            companies.clear();
            companies = readRecords();
            System.out.println("Choose the company:");
            companies.forEach(System.out::println);
        }
    }

    boolean isEmptyList() {
        companies.clear();
        companies = readRecords();
        return companies.isEmpty();
    }

    int chooseTheCompany() {
        getAll();
        Input input = new Input();
        int decision = input.takeUserDecision(0, companies.size());
        if (decision == 0) {
            return 0;
        }
        Company chosenCompany = companies.stream()
                .filter(company -> company.getId() == decision)
                .findAny().orElse(null);
        assert chosenCompany != null;
        System.out.println(chosenCompany.getId());
        return chosenCompany.getId();
    }

    void getCompanyName(int userDecision) {
        Company chosenCompany = companies.stream()
                .filter(company -> company.getId() == userDecision)
                .findAny().orElse(null);
        assert chosenCompany != null;
        System.out.println("'" + chosenCompany.getName() + "' company:");
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}