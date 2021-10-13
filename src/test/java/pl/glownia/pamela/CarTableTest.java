package pl.glownia.pamela;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class CarTableTest {
    List<Car> cars = new ArrayList<>();
    CarTable carTable = new CarTable(cars);

    @Test
    public void shouldBeFalseIfConditionsArentMet() {
        //given
        int carId = 0;
        //when
        boolean isAvailable = carTable.isAvailableForRent(carId);
        //then
        Assertions.assertFalse(isAvailable);
    }

    @Test
    public void shouldBeTrueIfCarIsAvailable() {
        //given
        cars.add(new Car(1, "Ford Mustang", 1, true));
        int carId = 1;

        //when
        boolean isAvailable = carTable.isAvailableForRent(carId);

        //then
        Assertions.assertTrue(isAvailable);
    }
}