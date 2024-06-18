package com.example.server.service;

import com.example.server.model.Subscription;
import com.example.server.model.User;
import com.example.server.repository.SubscriptionRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testSubscribe() {
        User subscriber = new User();
        User subscribeTo = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(subscriber));
        when(userRepository.findById(2)).thenReturn(Optional.of(subscribeTo));
        when(subscriptionRepository.existsBySubscriberAndSubscribeTo(subscriber, subscribeTo)).thenReturn(false);
        when(subscriptionRepository.save(Mockito.any(Subscription.class))).thenReturn(new Subscription());

        Subscription result = subscriptionService.subscribe(1, 2);

        assertNotNull(result);
    }

    @Test
    public void testUnsubscribe() {
        User subscriber = new User();
        User subscribeTo = new User();
        Subscription subscription = new Subscription();
        when(userRepository.findById(1)).thenReturn(Optional.of(subscriber));
        when(userRepository.findById(2)).thenReturn(Optional.of(subscribeTo));
        when(subscriptionRepository.findBySubscriberAndSubscribeTo(subscriber, subscribeTo)).thenReturn(Optional.of(subscription));

        assertDoesNotThrow(() -> subscriptionService.unsubscribe(1, 2));
    }

    @Test
    public void testCheckSubscription() {
        User subscriber = new User();
        User subscribeTo = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(subscriber));
        when(userRepository.findById(2)).thenReturn(Optional.of(subscribeTo));
        when(subscriptionRepository.existsBySubscriberAndSubscribeTo(subscriber, subscribeTo)).thenReturn(true);

        boolean result = subscriptionService.checkSubscription(1, 2);

        assertTrue(result);
    }
}