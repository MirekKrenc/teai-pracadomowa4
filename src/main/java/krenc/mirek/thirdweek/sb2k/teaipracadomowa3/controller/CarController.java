package krenc.mirek.thirdweek.sb2k.teaipracadomowa3.controller;

import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Car;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Color;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service.CarService;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service.CarServiceImpl;
import org.apache.coyote.http11.HttpOutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars()
    {
        Optional<List<Car>> allCars = carService.getAllCars();
        if (allCars.isPresent())
            return new ResponseEntity<>(allCars.get(), HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") long id)
    {
        Optional<Car> carById = carService.getCarById(id);
        if (carById.isPresent())
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}")
    public ResponseEntity<List<Car>> getCarsByColor(@PathVariable Color color)
    {
        Optional<List<Car>> optionalCarList = carService.getByColor(color);
        if (optionalCarList.isPresent() && optionalCarList.get().size()>0)
        {
            return new ResponseEntity<>(optionalCarList.get(), HttpStatus.OK);
        }
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car newCar)
    {
        return new ResponseEntity<>(carService.addCar(newCar), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Car> updateCa(@RequestBody Car car)
    {
        Car updated = carService.updateCar(car);
        if (car != null)
        {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteCar(@PathVariable long id)
    {
        return carService.deletCarById(id) == true ? new ResponseEntity<>(true, HttpStatus.OK) :
                new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{id}/{color}")
    public ResponseEntity<Car> modifyColor(@PathVariable long id, @PathVariable("color") Color newcolor)
    {
        Car car = carService.modifyColor(id, newcolor);
        if (car != null)
            return new ResponseEntity<>(car, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping
    public ResponseEntity<Car> modifyCar(@RequestBody Car car)
    {
       Car modified = carService.updateCarFields(car);
       if (modified != null)
           return new ResponseEntity<>(modified, HttpStatus.OK);
       else
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
