package pl.glownia.pamela;

import java.util.List;

public interface CarDao {
    void createTable(CarSharingJDBC dataBase);

    void addNewCar(int companyId);

    void insertRecordToTable(String carName, int companyId);

    List<Car> readRecords(int companyId);

    void getAll(int companyId);

    void getRentedCarInfo(int customerId);

    void closeConnection();
}