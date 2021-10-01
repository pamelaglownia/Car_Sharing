package pl.glownia.pamela;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CarDao implements Dao<Car> {
    private Connection connection;
    private List<Car> cars;

    public CarDao(List<Car> cars) {
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
    public void insertRecordToTable(CarSharingJDBC database, String carName) {
//TODO fix method
//        try {
//            Statement statement = connection.createStatement();
//            String recordToInsert = "INSERT INTO CAR (NAME, COMPANY_ID)" +
//                    "VALUES('" + carName + "'")";
//            statement.executeUpdate(recordToInsert);
//            System.out.println("The company was created!");
//            statement.close();
//        } catch (SQLException exception) {
//            exception.printStackTrace();
//        }
    }

    @Override
    public List<Car> readRecords(CarSharingJDBC database) {
        try {
            Statement statement = connection.createStatement();
            String recordToRead = "SELECT ID, NAME FROM CAR WHERE ID = COMPANY_ID";
            ResultSet resultSet = statement.executeQuery(recordToRead);
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int companyId = resultSet.getInt("COMPANY_ID");
                cars.add(new Car(id, name, companyId));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return cars;
    }

    @Override
    public void getAll(CarSharingJDBC database) {
        cars.clear();
        cars = readRecords(database);
        if (cars.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Car list:");
            cars.forEach(System.out::println);
        }
    }
}