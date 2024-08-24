package com.example.elolibrary.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="livro_id")
    private Long id;

    @Column(name="titulo")
    private String titulo;

    @Column(name="autor")
    private String autor;

    @Column(name="isbn")
    private String isbn;

    @Column(name="data_publicacao")
    private LocalDate dataPublicacao;

    @Column(name="categoria")
    private String categoria;
}
