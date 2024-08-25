package com.example.elolibrary.dto.output;

import com.example.elolibrary.interfaces.OutputDto;
import com.example.elolibrary.model.Usuario;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioOutputDto implements OutputDto<Usuario> {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataCadastro;
    private String telefone;

    @Override
    public OutputDto<Usuario> wrap(Usuario usuario) {
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
