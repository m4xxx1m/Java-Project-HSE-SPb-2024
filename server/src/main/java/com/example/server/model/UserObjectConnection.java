package com.example.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserObjectConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    int userId;

    int objectId;

    public UserObjectConnection() {}

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getObjectId() {
        return objectId;
    }

}
