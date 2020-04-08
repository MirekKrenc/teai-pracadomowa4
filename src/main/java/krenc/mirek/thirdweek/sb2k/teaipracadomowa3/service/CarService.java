package krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service;

import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Car;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Color;

import java.util.List;
import java.util.Optional;

public interface CarService {

    Optional<List<Car>> getAllCars();
    Optional<Car> getCarById(long id);
    Optional<List<Car>> getByColor(Color color);
    Car addCar(Car car);
    Car updateCar(Car car);
    Car modifyColor(long id, Color color);
    boolean deletCarById(long id);
    public void showAllCars();
    Car updateCarFields(Car car);

}
