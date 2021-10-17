package pl.glownia.pamela;

import java.util.List;

interface CustomerDao {
    void createTable(CarSharingJDBC dataBase);

    void insertRecordToTable(String customerName);

    List<Customer> readRecords();

    void getAll();

    void deleteCustomer();

    void closeConnection();
}
