package pl.glownia.pamela.customer;

import pl.glownia.pamela.DataBaseConnection;

import java.sql.*;
import java.util.List;

class CustomerRepository {

    private Connection connection;
    private final List<Customer> customers;

    CustomerRepository(List<Customer> customers) {
        this.customers = customers;
    }

    void createTable(DataBaseConnection dataBase) {
        connection = dataBase.getConnection();
        try {
            Statement creatingStatement = connection.createStatement();
            String table = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "HELPER_NUMBER INT NOT NULL, " +
                    "NAME VARCHAR NOT NULL, " +
                    "RENTED_CAR_ID INT DEFAULT NULL, " +
                    "RENTED_CAR_COMPANY INT DEFAULT NULL, " +
                    "FOREIGN KEY(RENTED_CAR_ID) REFERENCES CAR(ID) ON DELETE CASCADE)";
            creatingStatement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void insertRecordToTable(int customerId, String customerName) {
        try {
            PreparedStatement insertingStatement = connection.prepareStatement("INSERT INTO CUSTOMER (HELPER_NUMBER, NAME, RENTED_CAR_ID, RENTED_CAR_COMPANY) " +
                    "VALUES(?, ?, DEFAULT, DEFAULT)");
            insertingStatement.setInt(1, customerId);
            insertingStatement.setString(2, customerName);
            insertingStatement.executeUpdate();
            System.out.println("The customer was added!");
            insertingStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void rentACar(int customerId, int carId, int companyId) {
        try {
            PreparedStatement rentingStatement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ?, RENTED_CAR_COMPANY = ?  WHERE HELPER_NUMBER = ?");
            rentingStatement.setInt(1, carId);
            rentingStatement.setInt(2, companyId);
            rentingStatement.setInt(3, customerId);
            rentingStatement.executeUpdate();
            rentingStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    List<Customer> readRecords() {
        customers.clear();
        try {
            Statement readingStatement = connection.createStatement();
            String recordToRead = "SELECT HELPER_NUMBER, NAME, RENTED_CAR_ID, RENTED_CAR_COMPANY FROM CUSTOMER";
            ResultSet resultSet = readingStatement.executeQuery(recordToRead);
            while (resultSet.next()) {
                int customerId = resultSet.getInt("HELPER_NUMBER");
                String customerName = resultSet.getString("NAME");
                int rentedCarId = resultSet.getInt("RENTED_CAR_ID");
                int companyId = resultSet.getInt("RENTED_CAR_COMPANY");
                customers.add(new Customer(customerId, customerName, rentedCarId, companyId));
            }
            readingStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return customers;
    }

    void returnRentedCar(int carId, int customerId) {
        PreparedStatement returningStatement;
        ResultSet carToReturn;
        try {
            returningStatement = connection.prepareStatement("SELECT CAR.HELPER_NUMBER " +
                    "FROM CAR " +
                    "JOIN CUSTOMER " +
                    "ON CAR.HELPER_NUMBER = CUSTOMER.RENTED_CAR_ID " +
                    "WHERE CUSTOMER.HELPER_NUMBER = ?");
            returningStatement.setInt(1, customerId);
            carToReturn = returningStatement.executeQuery();
            if (!carToReturn.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                returningStatement = connection.prepareStatement("UPDATE CAR SET IS_AVAILABLE = ? " +
                        "WHERE HELPER_NUMBER = ?");
                returningStatement.setBoolean(1, true);
                returningStatement.setInt(2, carId);
                returningStatement.executeUpdate();
                returningStatement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ?, RENTED_CAR_COMPANY = ? " +
                        "WHERE HELPER_NUMBER = ?");
                returningStatement.setString(1, null);
                returningStatement.setString(2, null);
                returningStatement.setInt(3, customerId);
                returningStatement.executeUpdate();
                System.out.println("You returned a rented car.");
            }
            returningStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void deleteCustomer(int customerId) {
        try {
            PreparedStatement deletingStatement = connection.prepareStatement("DELETE FROM CUSTOMER WHERE HELPER_NUMBER = ?");
            deletingStatement.setInt(1, customerId);
            deletingStatement.executeUpdate();
            deletingStatement.close();
            updateCustomersId(customerId);
            System.out.println("Customer was deleted.");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void updateCustomersId(int customerId) {
        try {
            PreparedStatement updatingStatement = connection.prepareStatement("UPDATE CUSTOMER SET HELPER_NUMBER = HELPER_NUMBER - 1 WHERE HELPER_NUMBER > ?");
            updatingStatement.setInt(1, customerId);
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