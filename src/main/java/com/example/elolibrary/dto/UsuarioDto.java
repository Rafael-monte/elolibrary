package com.example.elolibrary.dto;

import com.example.elolibrary.model.Usuario;

public class UsuarioDto {
    private Long id;
    private String nome;
    private String email;
    private String dataCadastro;
    private String telefone;


    public UsuarioDto(Usuario usuario) {

    }
}
