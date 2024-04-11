package com.example.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rated_object")
public class RatedObject extends UserObjectConnection {

    public enum Type {
        LIKE,
        DISLIKE
    }

    private RatedObject.Type type;

    public RatedObject(int userId, int objectId, RatedObject.Type type) {
        this.userId = userId;
        this.objectId = objectId;
        this.type = type;
    }
    public RatedObject.Type getType() {
        return type;
    }

    public void setType (Type type) {
        this.type = type;
    }

}
