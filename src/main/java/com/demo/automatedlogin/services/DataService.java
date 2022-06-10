package com.demo.automatedlogin.services;

import com.demo.automatedlogin.dao.People;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DataService {

    private final ArrayList<People> peopleList = new ArrayList<>();
    private final AtomicInteger id = new AtomicInteger(1);

    @PostConstruct
    void init() {
        peopleList.add(People.builder().id(createId()).name("John Doe").age(25).build());
        peopleList.add(People.builder().id(createId()).name("Mary Lane").age(30).build());
        peopleList.add(People.builder().id(createId()).name("Jack Ripper").age(35).build());
    }

    private int createId() {
        return id.getAndIncrement();
    }

    public People create(People people) {
        people.setId(createId());
        peopleList.add(people);
        return people;
    }

    public Optional<People> update(int id, String name, int age) {
        Optional<People> people = get(id);
        if (people.isEmpty()) {
            return people;
        } else {
            People p = people.get();
            p.setName(name);
            p.setAge(age);
            return Optional.of(p);
        }
    }

    public boolean delete(int id) {
        Optional<People> people = get(id);
        if (people.isEmpty()) {
            return false;
        } else {
            People p = people.get();
            peopleList.remove(p);
            return true;
        }
    }

    public Optional<People> get(int id) {
        return peopleList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public List<People> listAll() {
        return Collections.unmodifiableList(peopleList);
    }
}
