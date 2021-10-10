package pl.glownia.pamela;

import java.util.List;

interface CustomerDao {
    void createTable(CarSharingJDBC dataBase);

    void addNewCustomer();

    void insertRecordToTable(String customerName);

    void rentACar(int customerId, int carId, int companyId);

    List<Customer> readRecords();

    void getAll();

    void returnRentedCar(int customerId);

    void closeConnection();
}
