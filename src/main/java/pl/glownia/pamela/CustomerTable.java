package pl.glownia.pamela;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

class CustomerTable implements CustomerDao {
    private Connection connection;
    private List<Customer> customers;

    CustomerTable(List<Customer> customers) {
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
                    "NAME VARCHAR UNIQUE NOT NULL, " +
                    "RENTED_CAR_ID INTEGER DEFAULT NULL, " +
                    "RENTED_CAR_COMPANY INTEGER DEFAULT NULL, " +
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
            String recordToInsert = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID, RENTED_CAR_COMPANY) " +
                    "VALUES('" + customerName + "', DEFAULT, DEFAULT)";
            statement.executeUpdate(recordToInsert);
            System.out.println("The customer was added!");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    boolean checkIfCustomerRentedACar(int customerId) {
        customers.clear();
        customers = readRecords();
        return customers.stream()
                .filter(customer -> customer.getId() == customerId)
                .anyMatch(customer -> customer.getCarId() >= 1);
    }

    @Override
    public void rentACar(int customerId, int carId, int companyId) {
        try {
            Statement statement = connection.createStatement();
            String recordToUpdate = "UPDATE CUSTOMER " +
                    "SET RENTED_CAR_ID = " + carId +
                    ", RENTED_CAR_COMPANY = " + companyId +
                    " WHERE ID = " + customerId;
            statement.executeUpdate(recordToUpdate);
            String carRecord = "UPDATE CAR SET IS_AVAILABLE = FALSE " +
                    "WHERE ID = " + carId + " AND COMPANY_ID = " + companyId;
            statement.executeUpdate(carRecord);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Customer> readRecords() {
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT ID, NAME, RENTED_CAR_ID, RENTED_CAR_COMPANY FROM CUSTOMER";
            ResultSet resultSet = statement.executeQuery(recordToRead);
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int carId = resultSet.getInt("RENTED_CAR_ID");
                int companyId = resultSet.getInt("RENTED_CAR_COMPANY");
                customers.add(new Customer(id, name, carId, companyId));
            }
            statement.close();
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

    private int getRentedCarId(int customerId) {
        customers = readRecords();
        return customers.stream()
                .filter(customer -> customer.getId() == customerId)
                .mapToInt(Customer::getCarId)
                .findFirst().orElse(0);
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
                String customerRecord = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL, RENTED_CAR_COMPANY = NULL " +
                        "WHERE ID = " + customerId;
                statement.executeUpdate(customerRecord);
                String carRecord = "UPDATE CAR SET IS_AVAILABLE = TRUE " +
                        "WHERE ID = " + getRentedCarId(customerId);
                statement.executeUpdate(carRecord);
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