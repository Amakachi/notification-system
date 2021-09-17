package com.pangaea.notificationsystem.services;

import com.google.gson.Gson;
import com.pangaea.notificationsystem.exceptions.DuplicateException;
import com.pangaea.notificationsystem.models.Response;
import com.pangaea.notificationsystem.models.Subscriber;
import com.pangaea.notificationsystem.models.dtos.PublishRequest;
import com.pangaea.notificationsystem.models.dtos.SubscribeRequest;
import com.pangaea.notificationsystem.models.dtos.SubscribeResponse;
import com.pangaea.notificationsystem.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private HttpClientService httpClientService;

    @Mock
    private Gson gson;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void subscribe_to_topic_with_unique_url() {
        Optional<Subscriber> optionalSubscriber = Optional.empty();
        when(notificationRepository.findByTopicAndUrl(anyString(), anyString())).thenReturn(optionalSubscriber);

        SubscribeResponse subscribeResponse = notificationService.subscribeToTopic("topic", new SubscribeRequest("http://localhost:9000/test1"));
        assertEquals( "http://localhost:9000/test1", subscribeResponse.getUrl());

    }

    @Test
    void subscribe_to_topic_with_duplicate_urls() {
        Optional<Subscriber> optionalSubscriber = Optional.of(new Subscriber("topic", "http://localhost:9000/test1"));
        when(notificationRepository.findByTopicAndUrl(anyString(), anyString())).thenReturn(optionalSubscriber);

        assertThrows(DuplicateException.class, () ->
                notificationService.subscribeToTopic("topic", new SubscribeRequest("http://localhost:9000/test1")));

    }

    @Test
    void publish_message_to_subcribers_success() throws IOException {
        List<Subscriber> subscriberList = Arrays.asList(new Subscriber("http://localhost:9000/test1", "topic"),
                new Subscriber("http://localhost:9000/test1", "topic"));
        Map<String, Object> message = Map.of("message", "hello");
        PublishRequest publishRequest = new PublishRequest("topic", message);

        when(notificationRepository.findByTopic(anyString())).thenReturn(subscriberList);

        when(gson.toJson(publishRequest)).thenReturn(publishRequest.toString());

        when(httpClientService.doPostRequest(anyString(), anyString())).thenReturn(200);

        ResponseEntity<Response> responseEntity = notificationService.publishMessage("topic", message);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void failed_to_publish_message() throws IOException {
        List<Subscriber> subscriberList = Arrays.asList(new Subscriber("http://localhost:9000/test1", "topic"),
                new Subscriber("http://localhost:9000/test1", "topic"));
        when(notificationRepository.findByTopic(anyString())).thenReturn(subscriberList);
        Map<String, Object> message = Map.of("message", "hello");
        PublishRequest publishRequest = new PublishRequest("topic", message);

        when(gson.toJson(publishRequest)).thenReturn(publishRequest.toString());

        when(httpClientService.doPostRequest(anyString(), anyString())).thenReturn(400);

        ResponseEntity<Response> responseEntity = notificationService.publishMessage("topic", message);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

    }
}