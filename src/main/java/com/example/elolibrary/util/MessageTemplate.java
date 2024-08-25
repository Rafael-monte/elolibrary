package com.example.elolibrary.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageTemplate {

    ENTITY_NOT_FOUND("Não foi possível encontrar o [%s] de id [%s]"),
    INVALID_EMAIL("O email [%s] fornecido não dentro do formato padrão"),
    EMAIL_ALREADY_IN_USE("O email [%s] já está sendo utilizado."),
    EMAIL_NOT_FOUND("Não foi possível encontrar o email [%s]"),
    NULL_FIELD("O campo [%s] é obrigatório"),
    INVALID_PHONE("O telefone [%s] é invalido"),
    PHONE_ALREADY_IN_USE("O telefone [%s] já está sendo utilizado."),
    REGISTER_DATE_AHEAD_OF_CURRENT_DATE("A data de cadastro [%s] é maior que a data atual."),
    PUBLISH_DATE_AHEAD_OF_CURRENT_DATE("A data de publicação [%s] é maior que a data atual."),
    DEVOLUTION_DATE_BEFORE_CURRENT_DATE("A data de devolução [%s] é menor que a data atual."),
    LOAN_DATE_AFTER_CURRENT_DATE("A data de empréstimo [%s] é maior que a data atual."),
    LOAN_DATE_AFTER_DEVOLUTION_DATE("A data de empréstimo [%s] é maior que a data de devolução"),
    INVALID_ISBN("A string de ISBN [%s] é inválida."),
    ISBN_ALREADY_IN_USE("O ISBN [%s] já está sendo utilizado."),
    ISBN_NOT_FOUND("Não foi possível encontrar o ISBN [%s]"),
    // TODO: Talvez seria legal implementar uma classe de protocolos para identificar estas situações, aí o número seria retornado aqui.
    COULD_NOT_ENCRYPT_PASSWORD("Não foi possível encriptar a senha informada, tente novamente mais tarde."),
    BOOK_LOAN_ALREADY_EXISTS("Já existe um empréstimo encaminhado para o livro solicitado (Data de entrega: %s %s)");
    private final String value;
}
