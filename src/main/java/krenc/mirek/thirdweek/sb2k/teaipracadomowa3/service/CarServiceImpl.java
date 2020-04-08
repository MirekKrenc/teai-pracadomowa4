package krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.service.MethodSet;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Car;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Color;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private List<Car> cars;

    public CarServiceImpl() {
        this.cars = new ArrayList<>();
        this.addCar(new Car(1, "Skoda", "Fabia", Color.RED));
        this.addCar(new Car(2, "Fiat", "Seicento", Color.BLUE));
        this.addCar(new Car(3, "Ford", "Fusion", Color.SILVER));
    }

    public void showAllCars()
    {
        if (cars != null)
        {
            cars.stream().forEach(System.out::println);
        }
    }

    @Override
    public Optional<List<Car>> getAllCars() {
        return Optional.of(cars);
    }

    @Override
    public Optional<Car> getCarById(long id) {
        Optional<Car> carOptional = cars.stream()
                .filter(car -> car.getId() == id)
                .findFirst();
        return carOptional;

    }

    @Override
    public Optional<List<Car>> getByColor(Color color) {
        return Optional.of(cars.stream()
                .filter(car -> car.getColor().equals(color))
                .collect(Collectors.toList()));
    }

    @Override
    public Car addCar(Car car) {
        this.cars.add(car);
        return car;
    }

    @Override
    public Car updateCar(Car car) {
        Optional<Car> foundCar = cars.stream()
                .filter(c -> c.getId() == car.getId())
                .findFirst();
        if (foundCar.isPresent())
        {
            cars.remove(foundCar.get());
            cars.add(car);
            return car;
        }
        else
        {
            return null;
        }
    }

    @Override
    public Car modifyColor(long id, Color color) {

        Optional<Car> foundCar = cars.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        if (foundCar.isPresent())
        {
            int index = cars.indexOf(foundCar.get());
            cars.get(index).setColor(color);
            return cars.get(index);
        }
        else
            return null;


    }

    @Override
    public boolean deletCarById(long id) {

        Optional<Car> foundCar = cars.stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (foundCar.isPresent())
        {
            cars.remove(foundCar.get());
            return true;
        }
        return false;
    }

    //reflection for changing only filed fields

    public Car updateCarFields(Car car)
    {
        int index = 0;
        //szukam czy jest samochód do updateu
        Optional<Car> carForUpdate = cars.stream()
                .filter(c -> c.getId() == car.getId())
                .findFirst();
        if (carForUpdate.isPresent())
        {
            index = cars.indexOf(carForUpdate.get());
            if (index < 0)
                return null;
        }
        else
        {
            return null;
        }

        //pobieram kolekcję metod getXXX bez getClass i bez getId
        List<String> getMethodsCollection = Arrays.stream(Car.class.getMethods())
                .filter(m -> m.getName().matches("get.*") && !m.getName().matches(".*Class") && !m.getName().matches(".*Id"))
                .map(m -> m.getName())
                .collect(Collectors.toList());

        //pobiream kolekcję metod setXXX(Type)
        Map<String, Type[]> setMethodsCollection = Arrays.stream(Car.class.getMethods())
                .filter(m -> m.getName().matches("set.*") && !m.getName().matches(".*Class") && !m.getName().matches(".*Id"))
                .collect(Collectors.toMap(Method::getName, Method::getGenericParameterTypes));

        MethodSet setMethodMap;

        for (String methodGetName: getMethodsCollection)
        {
            try {
                Method methodGet = Car.class.getMethod(methodGetName);
                setMethodMap = getSetMethodForClass(setMethodsCollection, methodGet.getName().replace("get", "set"));

                Method methodSet = Car.class.getMethod(setMethodMap.getName(), (Class<?>) setMethodMap.getType());
                //jesli metoda geXXX nie zwróci null to znaczy ze dane pole jest ustawione i gotowe do update'u
                if (methodGet.invoke(car) != null)
                {
                    methodSet.invoke(cars.get(index), methodGet.invoke(car));
                }

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        //jesli update się powiedzie to zwracam wygląd obiektu car z listy
        return cars.get(index);
    }

    private MethodSet getSetMethodForClass(Map<String, Type[]> setMethodsCollection, String name) {
        MethodSet result = new MethodSet();
        for (Map.Entry<String, Type[]> map: setMethodsCollection.entrySet())
        {
            try {
                Method setMethod = Car.class.getMethod(map.getKey(), (Class<?>) map.getValue()[0]);
                if (setMethod.getName().matches(".*" + name +".*"))
                {
                    result = new MethodSet(setMethod.getName(), setMethod.getGenericParameterTypes()[0]);
                    return result;
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Car applyPatchToCar(JsonPatch jsonPatch, Car targetCar)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = jsonPatch.apply(objectMapper.convertValue(targetCar, JsonNode.class));
            return objectMapper.treeToValue(jsonNode, Car.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}



