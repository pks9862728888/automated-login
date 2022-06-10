package com.demo.automatedlogin.controllers;

import com.demo.automatedlogin.dao.People;
import com.demo.automatedlogin.services.DataService;
import com.demo.automatedlogin.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class ResourcesController {

    @Autowired
    private DataService dataService;

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<People> create(@RequestBody People peopleData,
                                         @RequestHeader("Cookie") String cookie) {
        // 401 if session expired
        if (!loginService.checkLogin(cookie)) {
            return loginService.getUnauthorizedResponse();
        }

        // Create people
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dataService.create(peopleData));
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<People> find(@PathVariable("id") int id, @RequestHeader("Cookie") String cookie) {
        // 401 if session expired
        if (!loginService.checkLogin(cookie)) {
            return loginService.getUnauthorizedResponse();
        }

        // Find people
        Optional<People> people = dataService.get(id);
        if (people.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        } else {
            return ResponseEntity
                    .ok()
                    .body(people.get());
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") int id, @RequestHeader("Cookie") String cookie) {
        // 401 if session expired
        if (!loginService.checkLogin(cookie)) {
            return loginService.getUnauthorizedResponse();
        }

        // Delete people
        if (dataService.delete(id)) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<People>> create(@RequestHeader("Cookie") String cookie) {
        // 401 if session expired
        if (!loginService.checkLogin(cookie)) {
            return loginService.getUnauthorizedResponse();
        }

        // List all people
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dataService.listAll());
    }

}
