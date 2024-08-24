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
    @NotBlank(message="O email é obrigatório")
    @Email
    private String email;
    @NotBlank(message="O nome é obrigatório")
    private String nome;
    @NotBlank(message="A senha é obrigatória")
    private String senha;
    @NotNull(message="A data de cadastro é obrigatória")
    private LocalDate dataCadastro;
    @NotBlank(message="O telefone é obrigatório")
    @ValidBrazilianPhone
    private String telefone;

    @Override
    public Usuario toModel() {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setDataCadastro(dataCadastro);
        usuario.setTelefone(telefone);
        return usuario;
    }
}
