package com.aalechnovic.userdetailservice.domain;

import java.io.Serializable;
import java.util.Objects;

public record UserDetails(String email, String name, String surname) implements Serializable {

    @Override
    public String toString() {
        return "UserDetails{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetails that = (UserDetails) o;
        return Objects.equals(email, that.email) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname);
    }

}
