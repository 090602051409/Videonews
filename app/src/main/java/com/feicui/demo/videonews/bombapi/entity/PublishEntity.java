package com.feicui.demo.videonews.bombapi.entity;

import com.feicui.demo.videonews.bombapi.other.AuthorPointer;
import com.feicui.demo.videonews.bombapi.other.NewsPointer;

/**
 * Created by Administrator on 2016/12/27.
 */

public class PublishEntity {
    private String content;
    private AuthorPointer author;
    private NewsPointer news;

    public PublishEntity(String content, String userId, String newsId) {
        this.content = content;
        this.author = new AuthorPointer(userId);
        this.news = new NewsPointer(newsId);
    }
}
