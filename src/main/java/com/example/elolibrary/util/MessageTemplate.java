package com.example.elolibrary.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageTemplate {

    ENTITY_NOT_FOUND("Não foi possível encontrar o [%s] de id [%d]"),
    INVALID_EMAIL("O email [%s] fornecido não dentro do formato padrão"),
    NULL_FIELD("O campo [%s] é obrigatório"),
    INVALID_PHONE("O telefone [%s] é invalido"),
    REGISTER_DATE_AHEAD_OF_CURRENT_DATE("A data de cadastro [%s] é maior que a data atual.");
    private final String value;
}
