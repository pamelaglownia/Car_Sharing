package pl.glownia.pamela;

import java.sql.*;
import java.util.List;

class CarTable implements CarDao {
    private Connection connection;
    private List<Car> cars;

    CarTable(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public void createTable(CarSharingJDBC dataBase) {
        connection = dataBase.getConnection();
        try {
            //Execute a query
            Statement statement = connection.createStatement();
            String table = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL," +
                    "COMPANY_ID INT NOT NULL," +
                    "FOREIGN KEY(COMPANY_ID) REFERENCES COMPANY(ID))";
            statement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addNewCar(int companyId) {
        System.out.println("Enter the car name:");
        Input input = new Input();
        String carName = input.getNewItem();
        insertRecordToTable(carName, companyId);
        System.out.println();
    }

    @Override
    public void insertRecordToTable(String carName, int companyId) {
        try {
            Statement statement = connection.createStatement();
            String recordToInsert = "INSERT INTO CAR (NAME, COMPANY_ID)" +
                    "VALUES('" + carName + "', " + companyId + ")";
            statement.executeUpdate(recordToInsert);
            System.out.println("The car was created!");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<Car> readRecords(int companyId) {
        cars.clear();
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT NAME FROM CAR WHERE COMPANY_ID = " + companyId;
            ResultSet resultSet = statement.executeQuery(recordToRead);
            int id = 1;
            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                cars.add(new Car(id, name, companyId));
                id++;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return cars;
    }

    @Override
    public void getAll(int companyId) {
        cars.clear();
        cars = readRecords(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            cars.forEach(System.out::println);
        }
    }

    void getCarName(int carId) {
        Car rentedCar = cars.stream()
                .filter(car -> car.getId() == carId)
                .findAny().orElse(null);
        assert rentedCar != null;
        System.out.println("You rented " + rentedCar.getName() + ".");
    }

    int chooseTheCar(int companyId) {
        getAll(companyId);
        System.out.println("Choose the car:");
        Input input = new Input();
        int decision = input.takeUserDecision(0, cars.size());
        if (decision == 0) {
            return 0;
        }
        Car chosenCar = cars.stream()
                .filter(car -> car.getId() == decision)
                .findAny().orElse(null);
        assert chosenCar != null;
        return chosenCar.getId();
    }

    @Override
    public void getRentedCarInfo(int customerId) {
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT CAR.NAME, COMPANY.NAME AS COMPANY " +
                    "FROM CUSTOMER " +
                    "JOIN CAR " +
                    "ON CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                    "JOIN COMPANY " +
                    "ON CAR.COMPANY_ID = COMPANY.ID " +
                    "WHERE CUSTOMER.ID = " + customerId;
            ResultSet resultSet = statement.executeQuery(recordToRead);
            if (!resultSet.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                do {
                    String carName = resultSet.getString("NAME");
                    String companyName = resultSet.getString("COMPANY");
                    System.out.println("Your rented car: " + carName + "\ncompany: " + companyName);
                } while (resultSet.next());
            }
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