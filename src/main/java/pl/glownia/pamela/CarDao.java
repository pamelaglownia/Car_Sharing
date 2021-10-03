package pl.glownia.pamela;

import java.sql.SQLException;
import java.util.List;

public interface CarDao {
    void createTable(CarSharingJDBC dataBase);

    void addNewCar(int companyId);

    void insertRecordToTable(String carName, int companyId);

    List<Car> readRecords(int companyId);

    void getAll(int companyId);

    void closeConnection();
}
