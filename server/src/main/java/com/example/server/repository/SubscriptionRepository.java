package com.example.server.repository;

import com.example.server.model.Subscription;
import com.example.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findBySubscriber(User subscriber);

    boolean existsBySubscriberAndSubscribeTo(User subscriber, User subscribeTo);

    Optional<Subscription> findBySubscriberAndSubscribeTo(User subscriber, User subscribeTo);
}