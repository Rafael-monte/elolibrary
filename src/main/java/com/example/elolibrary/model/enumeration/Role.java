package com.example.elolibrary.model.enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Role {

    ADMIN("ADMIN"),
    USER("USER");

    private String value;

    public Optional<Role> of(String value) {
        return Stream.of(values()).filter(role -> role.value.equals(value)).findFirst();
    }
}
