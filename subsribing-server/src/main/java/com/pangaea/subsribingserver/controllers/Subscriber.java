package com.pangaea.subsribingserver.controllers;

import com.pangaea.subsribingserver.models.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class Subscriber {

    @PostMapping(value = "/test1")
    @ResponseStatus(HttpStatus.OK)
    public void subscribeTest1(@RequestBody Request request){
        log.info("Message received {}", request.getData());
    }

    @PostMapping(value = "/test2")
    @ResponseStatus(HttpStatus.OK)
    public void subscribeTest2(@RequestBody Request request){
        log.info("Message received {}", request.getData());
    }
}
