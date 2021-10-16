package pl.glownia.pamela;

import java.util.List;

interface CarDao {
    void createTable(CarSharingJDBC dataBase);

    void insertRecordToTable(String carName, int companyId);

    List<Car> readRecords(int companyId);

    void getAll(int companyId);

    void closeConnection();
}