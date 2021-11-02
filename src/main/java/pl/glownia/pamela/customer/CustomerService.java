package pl.glownia.pamela.customer;

import pl.glownia.pamela.DataBaseConnection;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private List<Customer> customers;
    CustomerRepository customerRepository;


    public CustomerService(DataBaseConnection dataBase) {
        customers = new ArrayList<>();
        customerRepository = new CustomerRepository(customers);
        customerRepository.createTable(dataBase);
    }

    public void addNewCustomer(String customerName) {
        customerRepository.insertRecordToTable(getCustomerId(), customerName);
    }

    private int getCustomerId() {
        customers = customerRepository.readRecords();
        if (customers.isEmpty()) {
            return 1;
        } else {
            return customers.size() + 1;
        }
    }

    public int getCustomersListSize() {
        customers = customerRepository.readRecords();
        return customers.size();
    }

    public void getAll() {
        customers = customerRepository.readRecords();
        if (customers.isEmpty()) {
            System.out.println("The customers list is empty!\n");
        } else {
            System.out.println("Customer list:");
            customers.forEach(System.out::println);
            System.out.println("0. Back");
        }
    }

    public int chooseTheCustomer(int customerId) {
        if (customers.isEmpty() || customerId == 0) {
            return 0;
        } else {
            return customers.get(customerId - 1).getId();
        }
    }

    public void deleteChosenCustomer(int customerId) {
        if (customerId != 0) {
            if (!carIsAlreadyRented(customerId)) {
                customerRepository.deleteCustomer(customerId);
            } else {
                System.out.println("You can't delete customer who rented a car.");
            }
        }
    }

    public boolean carIsAlreadyRented(int customerId) {
        customers = customerRepository.readRecords();
        return customers.stream()
                .filter(customer -> customer.getId() == customerId)
                .anyMatch(customer -> customer.getCarId() >= 1);
    }

    public void rentChosenCar(int customerId, int carId, int companyId) {
        customerRepository.rentACar(customerId, carId, companyId);
    }

    private int getRentedCarId(int customerId) {
        customers = customerRepository.readRecords();
        return customers.get(customerId - 1).getCarId();
    }


    public void returnRentedCar(int customerId) {
        int carId = getRentedCarId(customerId);
        customerRepository.returnRentedCar(carId, customerId);
    }

    public void closeCustomerConnection() {
        customerRepository.closeConnection();
    }
}