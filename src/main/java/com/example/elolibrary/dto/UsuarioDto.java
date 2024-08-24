package com.example.elolibrary.dto;

import com.example.elolibrary.interfaces.Dto;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.util.DateUtils;
import com.example.elolibrary.util.ServiceUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDto implements Dto<Usuario> {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataCadastro;
    private String telefone;

    @Override
    public Dto<Usuario> wrap(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();
        this.dataCadastro = usuario.getDataCadastro();
        return this;
    }

    @Override
    public Usuario unwrap() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setTelefone(this.telefone);
        usuario.setDataCadastro(this.dataCadastro);
        return usuario;
    }
}
