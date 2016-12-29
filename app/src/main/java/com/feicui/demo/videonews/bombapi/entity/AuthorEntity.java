package com.feicui.demo.videonews.bombapi.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/12/26.
 */

public class AuthorEntity {
    @SerializedName("__type")
    private String type;
    private String objectId;
    private String username;

    public String getType() {
        return type;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getUsername() {
        return username;
    }
}
