package com.pangaea.notificationsystem.repositories;

import com.pangaea.notificationsystem.models.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findByTopicAndUrl(String topic, String url);

    List<Subscriber> findByTopic(String topic);

}
