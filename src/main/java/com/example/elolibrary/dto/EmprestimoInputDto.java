package com.example.elolibrary.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoInputDto {
    private String emailUsuario;
    private String isbnLivro;
    private LocalDate dataDevolucao;
}
