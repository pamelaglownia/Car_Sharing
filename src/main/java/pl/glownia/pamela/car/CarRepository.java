package pl.glownia.pamela.car;

import pl.glownia.pamela.*;

import java.sql.*;
import java.util.List;

class CarRepository {

    private Connection connection;
    private final List<Car> cars;

    CarRepository(List<Car> cars) {
        this.cars = cars;
    }

    void createTable(DataBaseConnection dataBase) {
        connection = dataBase.getConnection();
        try {
            Statement creatingStatement = connection.createStatement();
            String table = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "HELPER_NUMBER INT NOT NULL, " +
                    "NAME VARCHAR NOT NULL, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "IS_AVAILABLE BOOLEAN, " +
                    "FOREIGN KEY(COMPANY_ID) REFERENCES COMPANY(ID) ON UPDATE CASCADE ON DELETE CASCADE)";
            creatingStatement.executeUpdate(table);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void insertRecordToTable(int carId, String carName, int companyId) {
        try {
            PreparedStatement insertingStatement = connection.prepareStatement("INSERT INTO CAR (HELPER_NUMBER, NAME, COMPANY_ID, IS_AVAILABLE) " +
                    "VALUES(?, ?, ?, ?)");
            insertingStatement.setInt(1, carId);
            insertingStatement.setString(2, carName);
            insertingStatement.setInt(3, companyId);
            insertingStatement.setBoolean(4, true);
            insertingStatement.executeUpdate();
            System.out.println("The car was created!");
            insertingStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    List<Car> readRecords(int companyId) {
        cars.clear();
        try {
            PreparedStatement readingStatement = connection.prepareStatement("SELECT HELPER_NUMBER, NAME, IS_AVAILABLE FROM CAR WHERE COMPANY_ID = ?");
            readingStatement.setInt(1, companyId);
            ResultSet car = readingStatement.executeQuery();
            while (car.next()) {
                int carId = car.getInt("HELPER_NUMBER");
                String carName = car.getString("NAME");
                boolean isCarAvailable = car.getBoolean("IS_AVAILABLE");
                cars.add(new Car(carId, carName, companyId, isCarAvailable));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return cars;
    }

    void updateInformationAboutCar(int customerId, int carId, int companyId) {
        try {
            PreparedStatement updatingStatement = connection.prepareStatement("UPDATE CAR SET IS_AVAILABLE = ? " +
                    "WHERE HELPER_NUMBER = ? AND COMPANY_ID = ?");
            updatingStatement.setBoolean(1, false);
            updatingStatement.setInt(2, carId);
            updatingStatement.setInt(3, companyId);
            updatingStatement.executeUpdate();
            updatingStatement.close();
            getRentedCarInfo(customerId);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void getRentedCarInfo(int customerId) {
        try {
            PreparedStatement readingStatement = connection.prepareStatement("SELECT CAR.NAME AS CAR_NAME, COMPANY.NAME AS NAME " +
                    "FROM CUSTOMER " +
                    "JOIN COMPANY " +
                    "ON COMPANY.HELPER_NUMBER = CUSTOMER.RENTED_CAR_COMPANY " +
                    "JOIN CAR " +
                    "ON CAR.COMPANY_ID = COMPANY.HELPER_NUMBER " +
                    "WHERE CUSTOMER.HELPER_NUMBER = ?");
            readingStatement.setInt(1, customerId);
            ResultSet rentedCar = readingStatement.executeQuery();
            if (!rentedCar.next()) {
                System.out.println("You didn't rent a car!");
            } else {
                String rentedCarName = rentedCar.getString("CAR_NAME");
                String companyName = rentedCar.getString("NAME");
                System.out.println("You rented: " + rentedCarName + ".");
                System.out.println("Company: " + companyName);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    void deleteCar(int carId, int companyId) {
        try {
            PreparedStatement deletingStatement = connection.prepareStatement("DELETE FROM CAR WHERE HELPER_NUMBER = ? AND COMPANY_ID = ?");
            deletingStatement.setInt(1, carId);
            deletingStatement.setInt(2, companyId);
            deletingStatement.executeUpdate();
            deletingStatement.close();
            updateCarsId(carId, companyId);
            System.out.println("Car was deleted.");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void updateCarsId(int carId, int companyId) {
        try {
            PreparedStatement updatingStatement = connection.prepareStatement("UPDATE CAR SET HELPER_NUMBER = HELPER_NUMBER-1 WHERE HELPER_NUMBER > ? AND COMPANY_ID = ?");
            updatingStatement.setInt(1, carId);
            updatingStatement.setInt(2, companyId);
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