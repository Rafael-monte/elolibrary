package com.example.elolibrary.model;

import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emprestimo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="livro_id")
    private Livro livro;

    @Column(name="data_emprestimo")
    private LocalDate dataEmprestimo;

    @Column(name="data_devolucao")
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private StatusEmprestimo status;

}
