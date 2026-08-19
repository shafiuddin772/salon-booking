package com.salon.user.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController  {

    @GetMapping
    public String HomeControllerHandler(){
        return "user Microservice is up and running";
    }
}
