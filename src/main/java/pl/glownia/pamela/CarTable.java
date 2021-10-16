package pl.glownia.pamela;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

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
                    "HELPER_NUMBER INT NOT NULL, " +
                    "NAME VARCHAR UNIQUE NOT NULL, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "IS_AVAILABLE BOOLEAN, " +
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
    }

    public int setId(int companyId) {
        cars = readRecords(companyId);
        List<Car> selectedCars = cars.stream()
                .filter(car -> car.getCompanyId() == companyId)
                .collect(Collectors.toList());
        if (selectedCars.isEmpty()) {
            return 1;
        } else {
            return selectedCars.size() + 1;
        }
    }

    @Override
    public void insertRecordToTable(String carName, int companyId) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO CAR (HELPER_NUMBER, NAME, COMPANY_ID, IS_AVAILABLE) " +
                    "VALUES(?, ?, ?, ?)");
            statement.setInt(1, setId(companyId));
            statement.setString(2, carName);
            statement.setInt(3, companyId);
            statement.setBoolean(4, true);
            statement.executeUpdate();
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
            PreparedStatement statement = connection.prepareStatement("SELECT HELPER_NUMBER, NAME, IS_AVAILABLE FROM CAR WHERE COMPANY_ID = ?");
            statement.setInt(1, companyId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("HELPER_NUMBER");
                String name = resultSet.getString("NAME");
                boolean isAvailable = resultSet.getBoolean("IS_AVAILABLE");
                cars.add(new Car(id, name, companyId, isAvailable));
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
        cars = readRecords(companyId);
        Car rentedCar = cars.stream()
                .filter(car -> car.getId() == carId && car.getCompanyId() == companyId)
                .findAny().orElse(null);
        assert rentedCar != null;
        System.out.println("You rented " + rentedCar.getName() + ".");
    }

    int chooseTheCar(int companyId) {
        if (companyId == 0) {
            return 0;
        } else if (isEmptyList(companyId)) {
            System.out.println("The car list is empty!");
            return 0;
        } else {
            getAll(companyId);
            System.out.println("Choose the car:");
            Input input = new Input();
            int chosenCar = input.takeUserDecision(0, cars.size());
            if (chosenCar == 0) {
                return 0;
            }
            boolean isAvailable = isAvailableForRent(chosenCar, companyId);
            while (!isAvailable) {
                System.out.println("This car is already taken. Choose other one or enter 0 to exit:");
                chosenCar = input.takeUserDecision(0, cars.size());
                if (chosenCar == 0) {
                    break;
                }
                isAvailable = isAvailableForRent(chosenCar, companyId);
            }
            int finallyDecision = chosenCar;
            return cars.stream()
                    .filter(car -> car.getId() == finallyDecision)
                    .mapToInt(Car::getId)
                    .findAny().orElse(0);
        }
    }

    boolean conditionsToRentAreMet(CompanyTable companyTable, CarTable carTable, int chosenCompany, int chosenCar) {
        return !companyTable.isEmptyList() && !carTable.isEmptyList(chosenCompany) && chosenCar != 0;
    }

    boolean isAvailableForRent(int carId, int companyId) {
        cars = readRecords(companyId);
        return cars.stream()
                .anyMatch(car -> car.getId() == carId && car.getCompanyId() == companyId && car.isAvailable());
    }

    void updateInformationAboutCar(int customerId, int carId, int companyId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE CAR SET IS_AVAILABLE = ? " +
                    "WHERE HELPER_NUMBER = ? AND COMPANY_ID = ?");
            statement.setBoolean(1, false);
            statement.setInt(2, carId);
            statement.setInt(3, companyId);
            statement.executeUpdate();
            statement.close();
            getRentedCarInfo(customerId);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void getRentedCarInfo(int customerId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT CUSTOMER.RENTED_CAR_ID, COMPANY.NAME AS NAME, COMPANY.ID AS COMPANY_ID " +
                    "FROM CUSTOMER " +
                    "JOIN COMPANY " +
                    "ON COMPANY.ID = CUSTOMER.RENTED_CAR_COMPANY " +
                    "JOIN CAR " +
                    "ON CAR.COMPANY_ID = COMPANY.ID " +
                    "WHERE CUSTOMER.ID = ?");
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                int carId = resultSet.getInt("RENTED_CAR_ID");
                int companyId = resultSet.getInt("COMPANY_ID");
                String companyName = resultSet.getString("NAME");
                getCarName(carId, companyId);
                System.out.println("Company: " + companyName);
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