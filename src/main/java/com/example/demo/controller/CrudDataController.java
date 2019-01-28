package com.example.demo.controller;

import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/crud-data")
public class CrudDataController {

    private final Logger logger = LoggerFactory.getLogger(CrudDataController.class);

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance hazelcastInstance;


    @PostMapping(value = "/save")
    public String saveDataToHazelcast(@RequestParam String key, @RequestParam String value) {
        logger.info("saveDataToHazelcast");
        Map<String, String> hazelcastMap = hazelcastInstance.getMap("dummyMap");
        hazelcastMap.put(key, value);
        return "Data added to node" + key;
    }

    @GetMapping(value = "/get")
    public String getDataFromHazelcast(@RequestParam String key) {
        logger.info("getDataFromHazelcast");
        Map<String, String> hazelcastMap = hazelcastInstance.getMap("dummyMap");
        return hazelcastMap.get(key);
    }

    @GetMapping(value = "/getAll")
    public Map<String, String> getAllDataFromHazelcast() {
        logger.info("getDataFromHazelcast");
        return hazelcastInstance.getMap("dummyMap");
    }

}