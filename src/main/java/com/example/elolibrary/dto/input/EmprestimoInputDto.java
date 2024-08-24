package com.example.elolibrary.dto.input;

import com.example.elolibrary.validators.ValidISBN;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoInputDto {
    @NotBlank(message = "O email é obrigatório")
    @Email
    private String emailUsuario;
    @NotBlank(message = "O ISBN do livro é obrigatório")
    @ValidISBN
    private String isbnLivro;
    @NotNull(message = "A data de devolução é obrigatória")
    private LocalDate dataDevolucao;
}
