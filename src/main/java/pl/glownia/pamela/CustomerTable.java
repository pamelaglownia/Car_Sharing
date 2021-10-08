package pl.glownia.pamela;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CustomerTable implements CustomerDao {
    private Connection connection;
    private List<Customer> customers;

    public CustomerTable(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public void createTable(CarSharingJDBC dataBase) {
        connection = dataBase.getConnection();
        try {
            //Execute a query
            Statement statement = connection.createStatement();
            String table = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL," +
                    "RENTED_CAR_ID INTEGER DEFAULT NULL," +
                    "FOREIGN KEY(RENTED_CAR_ID) REFERENCES CAR(ID))";
            statement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addNewCustomer() {
        System.out.println("Enter the customer name:");
        Input input = new Input();
        insertRecordToTable(input.getNewItem());
        System.out.println();
    }

    @Override
    public void insertRecordToTable(String customerName) {
        try {
            Statement statement = connection.createStatement();
            String recordToInsert = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) " +
                    "VALUES('" + customerName + "', DEFAULT)";
            statement.executeUpdate(recordToInsert);
            System.out.println("The customer was added!");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void rentACar(int customerId, int carId) {
        try {
            Statement statement = connection.createStatement();
            String recordToUpdate = "UPDATE CUSTOMER " +
                    "SET RENTED_CAR_ID = " + carId +
                    " WHERE ID = " + customerId;
            statement.executeUpdate(recordToUpdate);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Customer> readRecords() {
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER";
            ResultSet resultSet = statement.executeQuery(recordToRead);
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int carId = resultSet.getInt("RENTED_CAR_ID");
                customers.add(new Customer(id, name, carId));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return customers;
    }

    @Override
    public void getAll() {
        customers.clear();
        customers = readRecords();
        if (customers.isEmpty()) {
            System.out.println("The customers list is empty!\n");
        } else {
            System.out.println("Customer list:");
            customers.forEach(System.out::println);
            System.out.println("0. Back");
        }
    }

    @Override
    public void returnRentedCar(int customerId) {
        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            String recordToRead = "SELECT CAR.ID " +
                    "FROM CAR " +
                    "JOIN CUSTOMER " +
                    "ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                    "WHERE CUSTOMER.ID = " + customerId;
            resultSet = statement.executeQuery(recordToRead);
            if (!resultSet.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                String updateRecord = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL " +
                        "WHERE ID = " + customerId;
                statement.executeUpdate(updateRecord);
                System.out.println("You returned a rented car.");
            }
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    int chooseTheCustomer() {
        Input input = new Input();
        int decision = input.takeUserDecision(0, customers.size());
        return customers.stream()
                .filter(customer -> customer.getId() == decision)
                .mapToInt(Customer::getId)
                .findFirst().orElse(0);
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