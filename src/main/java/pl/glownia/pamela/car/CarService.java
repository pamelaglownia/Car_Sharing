package pl.glownia.pamela.car;

import pl.glownia.pamela.DataBaseConnection;
import pl.glownia.pamela.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {

    private List<Car> cars;
    private final CarRepository carRepository;
    private final Input input = new Input();

    public CarService(DataBaseConnection dataBase) {
        cars = new ArrayList<>();
        carRepository = new CarRepository(cars);
        carRepository.createTable(dataBase);
    }

    public void addNewCar(int companyId) {
        int carId = getCarHelperNumber(companyId);
        System.out.println("Enter the car name:");
        String carName = input.getNewItem();
        carRepository.insertRecordToTable(carId, carName, companyId);
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

    public void getAll(int companyId) {
        cars = carRepository.readRecords(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            cars.forEach(System.out::println);
            System.out.println("0. Back");
        }
    }

    public boolean isEmptyList(int companyId) {
        cars = carRepository.readRecords(companyId);
        return cars.isEmpty();
    }

    public int chooseTheCar(int companyId) {
        if (companyId == 0) {
            return 0;
        } else if (isEmptyList(companyId)) {
            System.out.println("The car list is empty!");
            return 0;
        } else {
            System.out.println("Choose the car:");
            getAll(companyId);
            int chosenCar = input.takeUserDecision(0, cars.size());
            if (chosenCar == 0) {
                return 0;
            }
            boolean isAvailable = isAvailableForRent(chosenCar, companyId);
            while (!isAvailable) {
                System.out.println("You can't choose this car. Choose other one or enter 0 to exit:");
                chosenCar = input.takeUserDecision(0, cars.size());
                if (chosenCar == 0) {
                    break;
                }
                isAvailable = isAvailableForRent(chosenCar, companyId);
            }
            return cars.get(chosenCar - 1).getId();
        }
    }

    private boolean isAvailableForRent(int carId, int companyId) {
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

    public void deleteChosenCar(int companyId) {
        int carId = chooseTheCar(companyId);
        if (isAvailableForRent(carId, companyId)) {
            carRepository.deleteCar(carId, companyId);
        }
    }

    public void closeCarConnection() {
        carRepository.closeConnection();
    }
}