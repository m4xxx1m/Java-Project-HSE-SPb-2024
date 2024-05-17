package com.example.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "saved_object")
public class SavedObject extends UserObjectConnection {

    public SavedObject() {
        type = Type.POST;
    }

    public enum Type {
        POST,
        COMMENT
    }

    private final SavedObject.Type type;

    public SavedObject(int userId, int objectId, SavedObject.Type type) {
        this.userId = userId;
        this.objectId = objectId;
        this.type = type;
    }
    public SavedObject.Type getType() {
        return type;
    }
}
