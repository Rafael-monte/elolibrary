package com.example.elolibrary.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum StatusEmprestimo {
    EM_USO("EM_USO"),
    ATRASADO("VENCIDO"),
    DEVOLVIDO("DEVOLVIDO");

    private final String value;

    public static Optional<StatusEmprestimo> of(String value) {
        return Stream.of(values()).filter(status -> status.getValue().equals(value)).findFirst();
    }
}
