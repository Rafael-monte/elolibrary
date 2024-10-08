package com.example.elolibrary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name="livro")
@AllArgsConstructor
@NoArgsConstructor
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="livro_id")
    private Long id;

    @Column(name="titulo")
    private String titulo;

    @Column(name="autor")
    private String autor;

    @Column(name="isbn", unique = true)
    private String isbn;

    @Column(name="data_publicacao")
    private LocalDate dataPublicacao;

    @Column(name="categoria")
    private String categoria;

    @Column(name="ativo")
    private Boolean ativo;
}
