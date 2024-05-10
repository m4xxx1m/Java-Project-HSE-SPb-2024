package com.example.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionId;

    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @JoinColumn(name = "subscribe_to_id")
    private User subscribeTo;

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    public User getSubscribeTo() {
        return subscribeTo;
    }

    public void setSubscribeTo(User subscribeTo) {
        this.subscribeTo = subscribeTo;
    }
}