package com.example.server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends ContentObj {

    private List<Long> comments = new ArrayList<>();

    public Post() {
        super();
    }
    public Post(long authorId, String content) {
        super(authorId, content);
    }

    public List<Long> getComments() {
        return comments;
    }

    public void setComments(List<Long> comments) {
        this.comments = comments;
    }

}
