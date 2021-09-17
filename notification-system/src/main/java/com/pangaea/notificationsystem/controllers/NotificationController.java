package com.pangaea.notificationsystem.controllers;

import com.pangaea.notificationsystem.models.Response;
import com.pangaea.notificationsystem.models.dtos.SubscribeRequest;
import com.pangaea.notificationsystem.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping(value = "subscribe/{topic}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void subscribeToTopic(@PathVariable String topic, @RequestBody SubscribeRequest subscribeRequest){
        notificationService.subscribeToTopic(topic, subscribeRequest);
    }

    @PostMapping(value = "publish/{topic}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> publishToTopic(@PathVariable String topic, @RequestBody Map<String, Object> request){
        return notificationService.publishMessage(topic, request);
    }
}
