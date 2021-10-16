package pl.glownia.pamela;

import java.sql.*;
import java.util.List;

class CompanyTable implements CompanyDao {
    private Connection connection;
    private List<Company> companies;

    CompanyTable(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public void createTable(CarSharingJDBC dataBase) {
        connection = dataBase.getConnection();
        try {
            //Execute a query
            Statement statement = connection.createStatement();
            String table = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL)";
            statement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void addNewCompany() {
        System.out.println("Enter the company name:");
        Input input = new Input();
        insertRecordToTable(input.getNewItem());
        System.out.println();
    }

    @Override
    public void insertRecordToTable(String companyName) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO COMPANY (NAME)" +
                    "VALUES(?)");
            statement.setString(1, companyName);
            statement.executeUpdate();
            System.out.println("The company was created!");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Company> readRecords() {
        companies.clear();
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
        companies = readRecords();
        System.out.println("Choose the company:");
        companies.forEach(System.out::println);
        System.out.println("0. Back");
    }

    boolean isEmptyList() {
        companies = readRecords();
        return companies.isEmpty();
    }

    int chooseTheCompany() {
        if (isEmptyList()) {
            System.out.println("The company list is empty!");
            return 0;
        } else {
            getAll();
        }
        Input input = new Input();
        int decision = input.takeUserDecision(0, companies.size());
        return companies.stream()
                .filter(company -> company.getId() == decision)
                .mapToInt(Company::getId)
                .findFirst().orElse(0);
    }

    void getCompanyName(int userDecision) {
        companies = readRecords();
        String chosenCompanyName = companies.stream()
                .filter(company -> company.getId() == userDecision)
                .map(Company::getName)
                .findFirst().orElse(null);
        System.out.println("'" + chosenCompanyName + "' company:");
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