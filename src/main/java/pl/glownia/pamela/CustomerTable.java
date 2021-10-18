package pl.glownia.pamela;

import java.sql.*;
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
                    "ID INT, " +
                    "NAME VARCHAR PRIMARY KEY NOT NULL, " +
                    "RENTED_CAR_ID INTEGER DEFAULT NULL, " +
                    "RENTED_CAR_COMPANY INTEGER DEFAULT NULL, " +
                    "FOREIGN KEY(RENTED_CAR_ID) REFERENCES CAR(ID) ON DELETE CASCADE ON UPDATE CASCADE)";
            statement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void addNewCustomer() {
        System.out.println("Enter the customer name:");
        Input input = new Input();
        insertRecordToTable(input.getNewItem());
    }

    int setCustomerId() {
        customers = readRecords();
        if (customers.isEmpty()) {
            return 1;
        } else {
            return customers.size() + 1;
        }
    }

    @Override
    public void insertRecordToTable(String customerName) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CUSTOMER (ID, NAME, RENTED_CAR_ID, RENTED_CAR_COMPANY) " +
                    "VALUES(?, ?, DEFAULT, DEFAULT)");
            statement.setInt(1, setCustomerId());
            statement.setString(2, customerName);
            statement.executeUpdate();
            System.out.println("The customer was added!");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    boolean customerRentedACar(int customerId) {
        customers = readRecords();
        return customers.stream()
                .filter(customer -> customer.getId() == customerId)
                .anyMatch(customer -> customer.getCarId() >= 1);
    }

    void rentACar(int customerId, int carId, int companyId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ?, RENTED_CAR_COMPANY = ?  WHERE ID = ?");
            statement.setInt(1, carId);
            statement.setInt(2, companyId);
            statement.setInt(3, customerId);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Customer> readRecords() {
        customers.clear();
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

    void returnRentedCar(int customerId) {
        PreparedStatement statement;
        ResultSet resultSet;
        try {
            statement = connection.prepareStatement("SELECT CAR.HELPER_NUMBER " +
                    "FROM CAR " +
                    "JOIN CUSTOMER " +
                    "ON CAR.HELPER_NUMBER = CUSTOMER.RENTED_CAR_ID " +
                    "WHERE CUSTOMER.ID = ?");
            statement.setInt(1, customerId);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                statement = connection.prepareStatement("UPDATE CAR SET IS_AVAILABLE = ? " +
                        "WHERE HELPER_NUMBER = ?");
                statement.setBoolean(1, true);
                statement.setInt(2, getRentedCarId(customerId));
                statement.executeUpdate();
                statement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ?, RENTED_CAR_COMPANY = ? " +
                        "WHERE ID = ?");
                statement.setString(1, null);
                statement.setString(2, null);
                statement.setInt(3, customerId);
                statement.executeUpdate();
                System.out.println("You returned a rented car.");
            }
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    int chooseTheCustomer() {
        getAll();
        if (customers.isEmpty()) {
            return 0;
        }
        Input input = new Input();
        int decision = input.takeUserDecision(0, customers.size());
        return customers.stream()
                .filter(customer -> customer.getId() == decision)
                .mapToInt(Customer::getId)
                .findFirst().orElse(0);
    }

    @Override
    public void deleteCustomer() {
        int customerId = chooseTheCustomer();
        if (customerId != 0) {
            if (!customerRentedACar(customerId)) {
                try {
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM CUSTOMER WHERE ID= ?");
                    statement.setInt(1, customerId);
                    statement.executeUpdate();
                    statement.close();
                    updateCustomersId(customerId);
                    System.out.println("Customer was deleted.");
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } else {
                System.out.println("You can't delete customer who rented a car.");
            }
        }
    }

    void updateCustomersId(int customerId) {
        customers = readRecords();
        for (Customer customer : customers) {
            if (customer.getId() > customerId) {
                try {
                    PreparedStatement statement = connection.prepareStatement("UPDATE CUSTOMER SET ID = ? WHERE ID > ?");
                    statement.setInt(1, customer.getId() - 1);
                    statement.setInt(2, customerId);
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