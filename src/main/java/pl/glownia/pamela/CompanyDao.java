package pl.glownia.pamela;

import java.util.List;

interface CompanyDao {
    void createTable(CarSharingJDBC dataBase);

    void insertRecordToTable(String companyName);

    List<Company> readRecords();

    void getAll();

    void closeConnection();
}