package krenc.mirek.thirdweek.sb2k.teaipracadomowa3;

import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Car;
import krenc.mirek.thirdweek.sb2k.teaipracadomowa3.model.Color;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class TeaiPracadomowa3Application {


    public static void main(String[] args) {
        SpringApplication.run(TeaiPracadomowa3Application.class, args);
    }


//    @EventListener(ApplicationReadyEvent.class)
//    public void method()
//    {
//        Car car = new Car(10, null, "Model", Color.RED);
//        List<String> getMethodsCollection = Arrays.stream(Car.class.getMethods())
//                .filter(m -> m.getName().matches("get.*") && !m.getName().matches(".*Class") && !m.getName().matches(".*Id"))
//                .map(m -> m.getName())
//                .collect(Collectors.toList());
//
//        Map<String, Type[]> setMethodsCollection = Arrays.stream(Car.class.getMethods())
//                .filter(m -> m.getName().matches("set.*") && !m.getName().matches(".*Class") && !m.getName().matches(".*Id"))
//                .collect(Collectors.toMap(Method::getName, Method::getGenericParameterTypes));
//
//
//        Car newCar = new Car();
//        MethodSet setMethodMap;
//
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//
//        for (String s: getMethodsCollection)
//        {
//            try {
//                Method methodGet = Car.class.getMethod(s);
//                setMethodMap = getSetMethodForClass(setMethodsCollection, methodGet.getName().replace("get", "set"));
//                System.out.println("MAPA:" + setMethodMap);
//                Method methodSet = Car.class.getMethod(setMethodMap.getName(), (Class<?>) setMethodMap.getType());
//                //System.out.println("SET:" + methodSet.getName());
//                if (methodGet.invoke(car) != null)
//                {
//                    System.out.println("->" + methodGet.invoke(car));
//                    methodSet.invoke(newCar, methodGet.invoke(car));
//                }
//
//            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println(newCar);
//
////        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
////        for (Method m: Car.class.getMethods())
////        {
////            System.out.println(m.getName());
////            System.out.println(m.getName().lastIndexOf(".") + " " + m.getName().length());
////        }
//
//
//    }
//
//    private MethodSet getSetMethodForClass(Map<String, Type[]> setMethodsCollection, String name) {
//        MethodSet result = new MethodSet();
//        for (Map.Entry<String, Type[]> map: setMethodsCollection.entrySet())
//        {
//            try {
//                Method setMethod = Car.class.getMethod(map.getKey(), (Class<?>) map.getValue()[0]);
//                if (setMethod.getName().matches(".*" + name +".*"))
//                {
//                    result = new MethodSet(setMethod.getName(), setMethod.getGenericParameterTypes()[0]);
//                    return result;
//                }
//
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

}

//class MethodSet {
//    private String name;
//    private Type type;
//
//    public MethodSet(String name, Type type) {
//        this.name = name;
//        this.type = type;
//    }
//
//    public MethodSet() {
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Type getType() {
//        return type;
//    }
//
//    public void setType(Type type) {
//        this.type = type;
//    }
//}
