package com.aalechnovic.userdetailservice.domain;

import java.io.Serializable;
import java.util.Objects;

public class UserDetails implements Serializable {

    private final String email;

    private final String name;

    private final String surname;

    public UserDetails(String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(email, name, surname);
    }
}
