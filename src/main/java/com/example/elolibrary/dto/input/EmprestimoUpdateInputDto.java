package com.example.elolibrary.dto.input;

import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoUpdateInputDto {
    @NotNull(message="Data de devolução obrigatoria")
    private LocalDate dataDevolucao;
    @NotNull(message="Data de emprestimo obrigatoria")
    private LocalDate dataEmprestimo;
    @NotNull(message = "Status do emprestimo obrigatório")
    private StatusEmprestimo status;
}
