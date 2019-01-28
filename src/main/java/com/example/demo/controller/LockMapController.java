package com.example.demo.controller;

import com.example.demo.pojo.Author;
import com.example.demo.pojo.Gender;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/lock-data-map")
public class LockMapController {

    private final Logger logger = LoggerFactory.getLogger(LockMapController.class);

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @RequestMapping("/lock")
    public void lockMapValue() throws Exception {
        Author lockAuthor = new Author("dummy-name", 0, Gender.MALE);
        Map<String, Author> authorMap = hazelcastInstance.getMap("authorMap");
        authorMap.put("lock-author", lockAuthor);

        ((IMap<String, Author>) authorMap).lock("lock-author");

        try {
            ((IMap<String, Author>) authorMap).remove("lock-author");
        } catch (Exception e) {
            throw new Exception("aa");
        } finally {
            ((IMap<String, Author>) authorMap).unlock("lock-author");
        }
    }
}
