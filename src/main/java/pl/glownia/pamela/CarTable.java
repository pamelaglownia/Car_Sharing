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
                    "NAME VARCHAR UNIQUE NOT NULL, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "IS_AVAILABLE BOOLEAN DEFAULT TRUE, " +
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
        insertRecordToTable(input.getNewItem(), companyId);
        System.out.println();
    }

    @Override
    public void insertRecordToTable(String carName, int companyId) {
        try {
            Statement statement = connection.createStatement();
            String recordToInsert = "INSERT INTO CAR (NAME, COMPANY_ID) " +
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
            String recordToRead = "SELECT NAME, IS_AVAILABLE FROM CAR WHERE COMPANY_ID = " + companyId;
            ResultSet resultSet = statement.executeQuery(recordToRead);
            int id = 1;
            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                boolean isAvailable = resultSet.getBoolean("IS_AVAILABLE");
                cars.add(new Car(id, name, companyId, isAvailable));
                id++;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return cars;
    }

    @Override
    public void getAll(int companyId) {
        cars = readRecords(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            cars.forEach(System.out::println);
        }
    }

    boolean isEmptyList(int companyId) {
        cars = readRecords(companyId);
        return cars.isEmpty();
    }

    void getCarName(int carId, int companyId) {
        Car rentedCar = cars.stream()
                .filter(car -> car.getId() == carId && car.getCompanyId() == companyId)
                .findAny().orElse(null);
        assert rentedCar != null;
        System.out.println("You rented " + rentedCar.getName() + ".");
    }

    int chooseTheCar(int companyId) {
        if (isEmptyList(companyId)) {
            System.out.println("The car list is empty!");
            return 0;
        } else {
            getAll(companyId);
            System.out.println("Choose the car:");
            Input input = new Input();
            int decision = input.takeUserDecision(0, cars.size());
            boolean isAvailable = isAvailableForRent(decision);
            while (!isAvailable) {
                System.out.println("This car is already taken. Choose other one or enter 0 to exit:");
                decision = input.takeUserDecision(0, cars.size());
                if (decision == 0) {
                    break;
                }
                isAvailable = isAvailableForRent(decision);
            }
            int finallyDecision = decision;
            return cars.stream()
                    .filter(car -> car.getId() == finallyDecision)
                    .mapToInt(Car::getId)
                    .findAny().orElse(0);
        }
    }

    boolean conditionsToRentAreMet(CompanyTable companyTable, CarTable carTable, int chosenCompany, int chosenCar) {
        return !companyTable.isEmptyList() && !carTable.isEmptyList(chosenCompany) && chosenCar != 0;
    }

    boolean isAvailableForRent(int carId) {
        return cars.stream()
                .filter(car -> car.getId() == carId)
                .anyMatch(Car::isAvailable);
    }

    @Override
    public void getRentedCarInfo(int customerId) {
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT CUSTOMER.RENTED_CAR_ID, COMPANY.NAME AS NAME, COMPANY.ID AS COMPANY_ID " +
                    "FROM CUSTOMER " +
                    "JOIN COMPANY " +
                    "ON COMPANY.ID = CUSTOMER.RENTED_CAR_COMPANY " +
                    "JOIN CAR " +
                    "ON CAR.COMPANY_ID = COMPANY.ID " +
                    "WHERE CUSTOMER.ID = " + customerId;
            ResultSet resultSet = statement.executeQuery(recordToRead);
            if (!resultSet.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                int carId = resultSet.getInt("RENTED_CAR_ID");
                int companyId = resultSet.getInt("COMPANY_ID");
                String companyName = resultSet.getString("NAME");
                getCarName(carId, companyId);
                System.out.println("Company:" + companyName);
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