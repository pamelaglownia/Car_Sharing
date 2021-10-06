package pl.glownia.pamela;

import java.util.List;

interface CustomerDao {
    void createTable(CarSharingJDBC dataBase);

    void addNewCustomer();

    void insertRecordToTable(String customerName);

    void rentACar(int customerId, int carId);

    List<Customer> readRecords();

    void getAll();

    void closeConnection();
}
