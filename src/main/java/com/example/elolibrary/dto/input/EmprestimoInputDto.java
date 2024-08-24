package com.example.elolibrary.dto.input;

import com.example.elolibrary.validators.ValidISBN;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoInputDto {
    @NotBlank(message = "Email obrigatorio")
    @Email
    private String emailUsuario;
    @NotBlank(message = "ISBN do livro obrigatorio")
    @ValidISBN
    private String isbnLivro;
    @NotNull(message = "Data de devolução obrigatoria")
    private LocalDate dataDevolucao;
}
