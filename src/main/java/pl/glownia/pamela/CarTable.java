package pl.glownia.pamela;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CarTable implements CarDao {
    private Connection connection;
    private List<Car> cars;

    public CarTable(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public void createTable(CarSharingJDBC dataBase) {
        connection = dataBase.getConnection();
        try {
            //Execute a query
            Statement statement = connection.createStatement();
            String table = "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR UNIQUE NOT NULL," +
                    "COMPANY_ID INTEGER NOT NULL," +
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
}