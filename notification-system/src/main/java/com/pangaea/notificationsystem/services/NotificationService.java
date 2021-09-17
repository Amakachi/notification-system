package com.pangaea.notificationsystem.services;

import com.google.gson.Gson;
import com.pangaea.notificationsystem.exceptions.DuplicateException;
import com.pangaea.notificationsystem.models.Response;
import com.pangaea.notificationsystem.models.Subscriber;
import com.pangaea.notificationsystem.models.dtos.PublishRequest;
import com.pangaea.notificationsystem.models.dtos.SubscribeRequest;
import com.pangaea.notificationsystem.models.dtos.SubscribeResponse;
import com.pangaea.notificationsystem.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final HttpClientService httpClientService;
    private final Gson gson;

    public SubscribeResponse subscribeToTopic(String topic, SubscribeRequest subscribeRequest){
        Optional<Subscriber> optionalSubscriber = notificationRepository.findByTopicAndUrl(topic, subscribeRequest.getUrl());

        if(optionalSubscriber.isPresent()){
            throw new DuplicateException("Topic exists with url " + subscribeRequest.getUrl());
        }

        Subscriber subscriber = new Subscriber(topic, subscribeRequest.getUrl());
        save(subscriber);

        return new SubscribeResponse(topic, subscribeRequest.getUrl());
    }

    public ResponseEntity<Response> publishMessage(String topic, Map<String, Object> request){
        Response response;
        List<String> notSentUrls = new ArrayList<>();
        List<Subscriber> subscriberList = notificationRepository.findByTopic(topic);
        PublishRequest publishRequest = new PublishRequest(topic, request);
        String json = gson.toJson(publishRequest);

        subscriberList.parallelStream().forEach(subscriber ->
            postRequestAndHandleResponse(notSentUrls, json, subscriber)
        );

        if (notSentUrls.isEmpty()){
            response =  Response.builder().message("success").build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response = Response.builder().topic(topic).message("message not publish to subscribers").data(notSentUrls).build();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private void postRequestAndHandleResponse(List<String> notSentUrls, String json, Subscriber subscriber) {
        try {
           int responseCode = httpClientService.doPostRequest(subscriber.getUrl(), json);
           if(responseCode != 200){
               notSentUrls.add(subscriber.getUrl());
           }
        } catch (IOException e) {
            log.info("An error occurred while publishing message to {}", subscriber.getTopic());
            notSentUrls.add(subscriber.getUrl());
        }
    }

    private void save(Subscriber subscriber){
        notificationRepository.save(subscriber);
    }


}
