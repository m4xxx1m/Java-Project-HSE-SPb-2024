package com.example.server.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {
    public Post(long authorId, String content) {
        super(authorId, content);
    }
}
