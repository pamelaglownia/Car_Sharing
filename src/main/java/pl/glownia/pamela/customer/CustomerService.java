package pl.glownia.pamela.customer;

import pl.glownia.pamela.DataBaseConnection;
import pl.glownia.pamela.Input;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private List<Customer> customers;
    CustomerRepository customerRepository;
    private final Input input = new Input();


    public CustomerService(DataBaseConnection dataBase) {
        customers = new ArrayList<>();
        customerRepository = new CustomerRepository(customers);
        customerRepository.createTable(dataBase);
    }

    public void addNewCustomer() {
        int customerId = getCustomerId();
        System.out.println("Enter the customer name:");
        String customerName = input.getNewItem();
        customerRepository.insertRecordToTable(customerId, customerName);
    }

    private int getCustomerId() {
        customers = customerRepository.readRecords();
        if (customers.isEmpty()) {
            return 1;
        } else {
            return customers.size() + 1;
        }
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

    public int chooseTheCustomer() {
        getAll();
        if (customers.isEmpty()) {
            return 0;
        }
        int decision = input.takeUserDecision(0, customers.size());
        return customers.stream()
                .filter(customer -> customer.getId() == decision)
                .mapToInt(Customer::getId)
                .findFirst().orElse(0);
    }

    public void deleteChosenCustomer() {
        int customerId = chooseTheCustomer();
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
        return customers.stream()
                .filter(customer -> customer.getId() == customerId)
                .mapToInt(Customer::getCarId)
                .findFirst().orElse(0);
    }


    public void returnRentedCar(int customerId) {
        int carId = getRentedCarId(customerId);
        customerRepository.returnRentedCar(carId, customerId);
    }

    public void closeCustomerConnection() {
        customerRepository.closeConnection();
    }
}