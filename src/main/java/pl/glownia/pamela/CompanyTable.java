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
                    "HELPER_NUMBER INT NOT NULL, " +
                    "NAME VARCHAR NOT NULL)";
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

    int setCompanyId() {
        companies = readRecords();
        if (companies.isEmpty()) {
            return 1;
        } else {
            return companies.size() + 1;
        }
    }

    @Override
    public void insertRecordToTable(String companyName) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO COMPANY (HELPER_NUMBER, NAME)" +
                    "VALUES(?, ?)");
            statement.setInt(1, setCompanyId());
            statement.setString(2, companyName);
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
            String recordToRead = "SELECT HELPER_NUMBER, NAME FROM COMPANY";
            ResultSet resultSet = statement.executeQuery(recordToRead);
            while (resultSet.next()) {
                int id = resultSet.getInt("HELPER_NUMBER");
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
    public void deleteCompany(int companyId) {
        if (companyId != 0) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM COMPANY WHERE HELPER_NUMBER = ?");
                statement.setInt(1, companyId);
                statement.executeUpdate();
                statement.close();
                updateCompaniesId(companyId);
                System.out.println("Company was deleted.");
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    void updateCompaniesId(int companyId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE COMPANY SET HELPER_NUMBER = HELPER_NUMBER-1 WHERE HELPER_NUMBER > ?");
            statement.setInt(1, companyId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
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