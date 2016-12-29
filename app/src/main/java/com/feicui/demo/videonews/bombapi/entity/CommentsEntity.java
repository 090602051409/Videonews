package com.feicui.demo.videonews.bombapi.entity;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/26.
 */

public class CommentsEntity {
    private String objectId;
    private String content;
    private AuthorEntity author;
    private Date createdAt;

    public String getObjectId() {
        return objectId;
    }

    public String getContent() {
        return content;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
