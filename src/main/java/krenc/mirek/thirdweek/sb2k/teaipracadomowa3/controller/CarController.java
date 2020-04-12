package krenc.mirek.thirdweek.sb2k.teaipracadomowa3.controller;

import com.github.fge.jsonpatch.JsonPatch;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Car;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Color;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String getIndex(){
        return "index";
    }


    @GetMapping(value = "/cars", produces =
            {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public String getCars(Model model)
    {
        Optional<List<Car>> allCars = carService.getAllCars();
        if (allCars.isPresent()) {
            allCars.get().forEach(car -> car.add(WebMvcLinkBuilder.linkTo(CarController.class).slash(car.getId()).withSelfRel()));
            Link linkCarList = WebMvcLinkBuilder.linkTo(CarController.class).withSelfRel();
            CollectionModel<Car> carCollectionModel = new CollectionModel<>(allCars.get(), linkCarList);
            model.addAttribute("carlist", allCars.get());
            model.addAttribute("newCar", new Car());
            return "cars";
        }
        else
            return "error";
    }

    @GetMapping(value = "/cars/{id}")
    public String getCarById(@PathVariable("id") long id, @ModelAttribute Car car, Model model)
    {
        Optional<Car> carById = carService.getCarById(id);
        if (carById.isPresent()) {
//            Link linkCar = WebMvcLinkBuilder.linkTo(CarController.class).slash(carById.get().getId()).withSelfRel();
//            carById.get().add(linkCar);
            model.addAttribute("existingCar", carById.get());
            return "editcar";
        }
        else
            return "error";
    }

    @GetMapping(value = "/cars/color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE,
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

    @GetMapping("/cars/add")
    public String addCar(Model model)
    {
        System.out.println("W metodzie GET");
        model.addAttribute("newCar", new Car());
        return "addcar";
    }
    @PostMapping("/cars/add")
    public String addCar(Car newCar)
    {
        newCar.setId(carService.calculateNextId());
        carService.addCar(newCar);
        System.out.println("W metodzie POST");
        return "redirect:/cars";
    }

    @PostMapping(value="/cars")
    public String updateCar(Car car, @RequestHeader Map<String, String> headers)
    {
        headers.forEach((key, value) -> System.out.println(key + "->" + value));
        System.out.println("W metodzie POST:" + car);
        Car updated = carService.updateCar(car);
        if (car != null)
        {
            return "redirect:/cars";
        }
        else
            return "error";
    }

    @DeleteMapping(value = "/cars/{id}")
    public ResponseEntity<Boolean> deleteCar(@PathVariable long id)
    {

        return carService.deletCarById(id) == true ? new ResponseEntity<>(true, HttpStatus.OK) :
                new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    //endpoint do update koloru - tylko
    @PatchMapping(value = "/cars/{id}/{color}")
    public ResponseEntity<Car> modifyColor(@PathVariable long id, @PathVariable("color") Color newcolor)
    {
        Car car = carService.modifyColor(id, newcolor);
        if (car != null)
            return new ResponseEntity<>(car, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //endpoint do update wszytkich atrybutów - w zależności od ustawienia pól w przekazywanym obiekcie
    @PatchMapping("/cars")
    public ResponseEntity<Car> modifyCar(@RequestBody Car car)
    {
       Car modified = carService.updateCarFields(car);
       if (modified != null)
           return new ResponseEntity<>(modified, HttpStatus.OK);
       else
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //endpoint do modyfikacji pól z wykorzystaniem JsonPatch
    @PatchMapping(value = "/cars/{id}", consumes = "application/json-patch+json",
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
