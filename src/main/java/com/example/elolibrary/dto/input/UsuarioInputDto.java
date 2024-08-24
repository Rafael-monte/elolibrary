package com.example.elolibrary.dto.input;

import com.example.elolibrary.interfaces.InputDto;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.validators.ValidBrazilianPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioInputDto implements InputDto<Usuario> {
    @NotBlank(message="Email obrigatório")
    @Email(message="Email invalido informado")
    private String email;
    @NotBlank(message="Nome e obrigatorio")
    private String nome;
    @NotBlank(message="Senha obrigatoria")
    private String senha;
    @NotBlank(message="Telefone obrigatorio")
    @ValidBrazilianPhone
    private String telefone;

    @Override
    public Usuario toModel() {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setTelefone(telefone);
        return usuario;
    }
}
