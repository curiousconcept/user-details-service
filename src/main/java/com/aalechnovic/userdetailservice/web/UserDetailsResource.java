package com.aalechnovic.userdetailservice.web;

import com.aalechnovic.userdetailservice.domain.UserDetails;

public class UserDetailsResource extends UserDetails {

    private long id;

    public UserDetailsResource(String email, String name, String surname) {
        super(email, name, surname);
    }

    public long getId() {
        return id;
    }
}
