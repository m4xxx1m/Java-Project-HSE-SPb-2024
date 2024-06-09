package com.example.server.dto;

public class PostDto extends ContentObjDto {

    private final String tags;
    private final String title;
    private final int commentsCount;
    private final boolean addResume;

    public PostDto(int authorId, String title, String content, String tags, int commentsCount, boolean addResume) {
        super(authorId, content, -1);
        this.tags = tags;
        this.title = title;
        this.commentsCount = commentsCount;
        this.addResume = addResume;
    }

    public String getTitle() {
        return title;
    }

    public String getTags() {
        return tags;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public boolean isResumeNeeded() {
        return addResume;
    }

}
