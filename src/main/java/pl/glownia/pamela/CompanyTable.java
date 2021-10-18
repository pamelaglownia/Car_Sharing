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
                    "ID INT NOT NULL, " +
                    "NAME VARCHAR PRIMARY KEY NOT NULL)";
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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO COMPANY (ID, NAME)" +
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
    public void deleteCompany(int companyId) {
        if (companyId != 0) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM COMPANY WHERE ID= ?");
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

    private void updateCompaniesId(int companyId) {
        companies = readRecords();
        for (Company company : companies) {
            if (company.getId() > companyId) {
                try {
                    PreparedStatement statement = connection.prepareStatement("UPDATE COMPANY SET ID = ? WHERE ID > ?");
                    statement.setInt(1, company.getId() - 1);
                    statement.setInt(2, companyId);
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
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