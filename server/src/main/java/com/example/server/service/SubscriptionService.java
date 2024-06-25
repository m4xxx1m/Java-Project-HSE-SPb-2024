package com.example.server.service;

import com.example.server.model.Post;
import com.example.server.model.Subscription;
import com.example.server.model.User;
import com.example.server.repository.PostRepository;
import com.example.server.repository.SubscriptionRepository;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            UserRepository userRepository,
            PostRepository postRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Subscription subscribe(Integer subscriberId, Integer subscribeToId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));
        User subscribeTo = userRepository.findById(subscribeToId)
                .orElseThrow(() -> new RuntimeException("User to subscribe to not found"));

        if (subscriptionRepository.existsBySubscriberAndSubscribeTo(subscriber, subscribeTo)) {
//            throw new RuntimeException("Already subscribed");
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setSubscribeTo(subscribeTo);

        return subscriptionRepository.save(subscription);
    }

    public void unsubscribe(Integer subscriberId, Integer subscribeToId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));
        User subscribeTo = userRepository.findById(subscribeToId)
                .orElseThrow(() -> new RuntimeException("User to unsubscribe from not found"));

//        Subscription subscription = subscriptionRepository.findBySubscriberAndSubscribeTo(subscriber, subscribeTo)
//                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Optional<Subscription> subscription = subscriptionRepository.findBySubscriberAndSubscribeTo(subscriber, subscribeTo);

        subscription.ifPresent(subscriptionRepository::delete);
    }

    public boolean checkSubscription(Integer subscriberId, Integer subscribeToId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));
        User subscribeTo = userRepository.findById(subscribeToId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));

        return subscriptionRepository.existsBySubscriberAndSubscribeTo(subscriber, subscribeTo);
    }

    public List<User> getSubscriptions(Integer subscriberId) {
        User subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));

        return subscriptionRepository
                .findBySubscriber(subscriber)
                .stream()
                .map(Subscription::getSubscribeTo)
                .collect(Collectors.toList());
    }

    public List<Post> getPostsFromSubscriptions(Integer subscriberId) {
        return getSubscriptions(subscriberId)
                .stream()
                .flatMap(user -> postRepository.findByAuthorId(user.getUserId()).stream())
                .sorted(Comparator.comparing(Post::getCreationTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
