package krenc.mirek.thirdweek.sb2k.teaipracadomowa3.controller;

import com.github.fge.jsonpatch.JsonPatch;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Car;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Color;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service.CarService;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service.CarServiceImpl;
import org.apache.coyote.http11.HttpOutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(produces =
            {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<List<Car>>> getCars()
    {

        Optional<List<Car>> allCars = carService.getAllCars();
        if (allCars.isPresent()) {
            allCars.get().forEach(car -> car.add(WebMvcLinkBuilder.linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            Link linkCarList = WebMvcLinkBuilder.linkTo(CarController.class).withSelfRel();
            CollectionModel<Car> carCollectionModel = new CollectionModel<>(allCars.get(), linkCarList);
            return new ResponseEntity(carCollectionModel, HttpStatus.OK);
        }
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable("id") long id)
    {
        Optional<Car> carById = carService.getCarById(id);
        if (carById.isPresent()) {
            Link linkCar = WebMvcLinkBuilder.linkTo(CarController.class).slash(carById.get().getId()).withSelfRel();
            carById.get().add(linkCar);
            return new ResponseEntity(carById.get(), HttpStatus.OK);
        }
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_XML_VALUE})
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

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> addCar(@RequestBody Car newCar)
    {
        return new ResponseEntity<>(carService.addCar(newCar), HttpStatus.CREATED);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_XML_VALUE})
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

    //endpoint do update koloru - tylko
    @PatchMapping(value = "/{id}/{color}")
    public ResponseEntity<Car> modifyColor(@PathVariable long id, @PathVariable("color") Color newcolor)
    {
        Car car = carService.modifyColor(id, newcolor);
        if (car != null)
            return new ResponseEntity<>(car, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //endpoint do update wszytkich atrybutów - w zależności od ustawienia pól w przekazywanym obiekcie
    @PatchMapping
    public ResponseEntity<Car> modifyCar(@RequestBody Car car)
    {
       Car modified = carService.updateCarFields(car);
       if (modified != null)
           return new ResponseEntity<>(modified, HttpStatus.OK);
       else
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //endpoint do modyfikacji pól z wykorzystaniem JsonPatch
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> modifyCarJSON(@PathVariable long id, @RequestBody JsonPatch patch)
    {
        Optional<Car> carForUpdate = carService.getCarById(id);
        if (carForUpdate.isPresent()) {
            int index = carService.getAllCars().get().indexOf(carForUpdate.get());
            //obsluga JsonPatch
            Car newDataCar = carService.applyPatchToCar(patch, carForUpdate.get());
            carService.getAllCars().get().set(index, newDataCar);
            return ResponseEntity.ok(newDataCar);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }


}
