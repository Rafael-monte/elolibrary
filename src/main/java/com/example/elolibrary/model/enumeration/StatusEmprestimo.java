package com.example.elolibrary.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum StatusEmprestimo {
    EM_DIA("EM_DIA"),
    VENCIDO("VENCIDO");

    private final String value;

    public static Optional<StatusEmprestimo> of(String value) {
        return Stream.of(values()).filter(status -> status.getValue().equals(value)).findFirst();
    }
}
