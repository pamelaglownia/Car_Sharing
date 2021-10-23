package pl.glownia.pamela.company;

import pl.glownia.pamela.DataBaseConnection;

import java.sql.*;
import java.util.List;

class CompanyRepository {

    private Connection connection;
    private final List<Company> companies;

    CompanyRepository(List<Company> companies) {
        this.companies = companies;
    }

    void createTable(DataBaseConnection dataBase) {
        connection = dataBase.getConnection();
        try {
            Statement creatingStatement = connection.createStatement();
            String companyTable = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT NOT NULL, " +
                    "NAME VARCHAR NOT NULL)";
            creatingStatement.executeUpdate(companyTable);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void insertRecordToTable(int companyId, String companyName) {
        try {
            PreparedStatement insertingStatement = connection.prepareStatement("INSERT INTO COMPANY (ID, NAME)" +
                    "VALUES(?, ?)");
            insertingStatement.setInt(1, companyId);
            insertingStatement.setString(2, companyName);
            insertingStatement.executeUpdate();
            System.out.println("The company was created!");
            insertingStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    List<Company> readRecords() {
        companies.clear();
        try {
            Statement readingStatement = connection.createStatement();
            String recordToRead = "SELECT ID, NAME FROM COMPANY";
            ResultSet company = readingStatement.executeQuery(recordToRead);
            while (company.next()) {
                int companyId = company.getInt("ID");
                String companyName = company.getString("NAME");
                companies.add(new Company(companyId, companyName));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return companies;
    }

    void deleteCompany(int companyId) {
        try {
            PreparedStatement deletingStatement = connection.prepareStatement("DELETE FROM COMPANY " +
                    "WHERE ID = ?");
            deletingStatement.setInt(1, companyId);
            deletingStatement.executeUpdate();
            deletingStatement.close();
            updateCompaniesId(companyId);
            System.out.println("Company was deleted.");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void updateCompaniesId(int companyId) {
        try {
            PreparedStatement updatingStatement = connection.prepareStatement("UPDATE COMPANY SET ID = ID-1 " +
                    "WHERE ID > ?");
            updatingStatement.setInt(1, companyId);
            updatingStatement.executeUpdate();
            updatingStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}