package com.example.elolibrary.model;

import com.example.elolibrary.model.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name="usuario")
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="usuario_id")
    private Long id;

    @Column(name="nome")
    private String nome;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="data_cadastro")
    private LocalDate dataCadastro;

    @Column(name="telefone", unique = true)
    private String telefone;

    @Column(name="senha")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;

    @Column(name="ativo")
    private Boolean ativo;
}
