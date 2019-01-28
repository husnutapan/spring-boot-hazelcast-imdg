package com.example.demo.controller;


import com.example.demo.pojo.Author;
import com.example.demo.pojo.Gender;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.SqlPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/query-data")
public class HazelcastQueryController implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(HazelcastQueryController.class);

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @GetMapping(value = "/getAuthList")
    public Set<Author> getAuthorListAgeGreaterThanWithParam(@RequestParam("age") int age) {
        IMap authorMap = hazelcastInstance.getMap("authorMap");
        Set<Author> authorSet = (Set<Author>) authorMap.values(new SqlPredicate("age >edan" + age));
        return authorSet;
    }


    @GetMapping(value = "/getGenderAuthList")
    public Set<Author> getGenderAuthListWithParam(@RequestParam("gender") Gender gender) {
        IMap authorMap = hazelcastInstance.getMap("authorMap");
        Set<Author> authorSet = (Set<Author>) authorMap.values(new SqlPredicate("gender ==" + gender));
        return authorSet;
    }

    @GetMapping(value = "/getNameLike")
    public Set<Author> getNameLikeWithParam(@RequestParam("likeText") String text) {
        IMap authorMap = hazelcastInstance.getMap("authorMap");
        Set<Author> authorSet = (Set<Author>) authorMap.values(Predicates.like("name", "%" + text + "%"));
        return authorSet;
    }

    @GetMapping(value = "/getBetweenTwoAges")
    public Set<Author> getBetweenTwoAgesWitbParam(@RequestParam("age1") int age1, @RequestParam("age2") int age2) {
        IMap authorMap = hazelcastInstance.getMap("authorMap");
        Set<Author> authorSet = (Set<Author>) authorMap.values(Predicates.between("age", (Comparable) age1, (Comparable) age2));
        return authorSet;
    }


    @Override
    public void run(String... args) throws Exception {
        Map<String, Author> authorMap = hazelcastInstance.getMap("authorMap");
        for (int i = 0; i < 100; i++) {
            Author author;
            if (i % 2 == 0) {
                author = new Author("name" + i, i + 10, Gender.MALE);
            } else {
                author = new Author("name" + i, i + 10, Gender.FEMALE);
            }
            authorMap.put(author.getName(), author);
        }
    }
}
