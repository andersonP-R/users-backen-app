package com.anderson.users.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJSONCreator {

    @JsonCreator
    public SimpleGrantedAuthorityJSONCreator(@JsonProperty("authority") String role) { // indicate where to take role var with @JsonProperty annotation

    }
}
