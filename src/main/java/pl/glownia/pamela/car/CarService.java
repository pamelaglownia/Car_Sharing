package pl.glownia.pamela.car;

import pl.glownia.pamela.DataBaseConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {

    private List<Car> cars;
    private final CarRepository carRepository;

    public CarService(DataBaseConnection dataBase) {
        cars = new ArrayList<>();
        carRepository = new CarRepository(cars);
        carRepository.createTable(dataBase);
    }

    public void addNewCar(String carName, int companyId) {
        carRepository.insertRecordToTable(getCarHelperNumber(companyId), carName, companyId);
    }

    private int getCarHelperNumber(int companyId) {
        cars = carRepository.readRecords(companyId);
        List<Car> selectedCars = cars.stream()
                .filter(car -> car.getCompanyId() == companyId)
                .collect(Collectors.toList());
        if (selectedCars.isEmpty()) {
            return 1;
        } else {
            return selectedCars.size() + 1;
        }
    }

    public int getCarsListSize(int companyId) {
        cars = carRepository.readRecords(companyId);
        return cars.size();
    }

    public void getAll(int companyId) {
        cars = carRepository.readRecords(companyId);
        if (!isEmptyList(companyId)) {
            System.out.println("Car list:");
            cars.forEach(System.out::println);
        }
    }

    public boolean isEmptyList(int companyId) {
        cars = carRepository.readRecords(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            return true;
        }
        return false;
    }

    public int chooseTheCar(int companyId, int chosenCar) {
        if (cars.isEmpty() || companyId == 0) {
            return 0;
        } else {
            if (chosenCar == 0) {
                return 0;
            }
        }
        return cars.get(chosenCar - 1).getId();
    }

    public boolean isAvailableForRent(int carId, int companyId) {
        cars = carRepository.readRecords(companyId);
        return cars.stream()
                .anyMatch(car -> car.getId() == carId && car.getCompanyId() == companyId && car.isAvailable());
    }

    public void getInfoAboutRentedCar(int customerId) {
        carRepository.getRentedCarInfo(customerId);
    }

    public void updateInformationAfterRentingCar(int customerId, int carId, int companyId) {
        carRepository.updateInformationAboutCar(customerId, carId, companyId);
    }

    public void deleteChosenCar(int carId, int companyId) {
        if (isAvailableForRent(carId, companyId)) {
            carRepository.deleteCar(carId, companyId);
        } else {
            System.out.println("You can't delete rented car.");
        }
    }

    public void closeCarConnection() {
        carRepository.closeConnection();
    }
}