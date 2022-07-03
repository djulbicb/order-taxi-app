//package com.djulb.engine;
//
//import com.djulb.way.bojan.Coordinate;
//import com.djulb.way.elements.Taxi;
//import com.sun.codemodel.JCodeModel;
//import org.jsonschema2pojo.*;
//import org.jsonschema2pojo.rules.RuleFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//public class FakeCarController {
//    @Autowired
//    ManagerTaxi managerTaxi;
//    @GetMapping("/test")
//    public String test () throws IOException {
//        return "sss";
//    }
//
//
//    @GetMapping("/waypoints")
//    public List<Object[]> getWaypoints () throws IOException {
//        return managerTaxi.getCarById("id").get().getCurrentRoutePath().get().getPathArray();
//        // return pathBank.getPathArray();
//    }
//
//    @GetMapping("/point")
//    public List<Double> point () throws IOException {
//        Optional<Taxi> id = managerTaxi.getCarById("id");
//        Coordinate currentPosition = id.get().getCurrentPosition();
//
//        return List.of(currentPosition.getLat(),currentPosition.getLng());
//        // return pathBank.getPoint().get();
//    }
//
//
//}
