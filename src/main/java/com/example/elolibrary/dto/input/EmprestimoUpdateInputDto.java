package com.example.elolibrary.dto.input;

import com.example.elolibrary.interfaces.InputDto;
import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import com.example.elolibrary.validators.ValidISBN;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoUpdateInputDto {
    @NotNull(message="A data de devolução é obrigatória")
    private LocalDate dataDevolucao;
    @NotNull(message="A data de emprestimo é obrigatória")
    private LocalDate dataEmprestimo;
    @NotNull(message = "O status do emprestimo é obrigatório")
    private StatusEmprestimo status;
}
