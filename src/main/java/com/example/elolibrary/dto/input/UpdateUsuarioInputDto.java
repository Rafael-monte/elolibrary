package com.example.elolibrary.dto.input;

import com.example.elolibrary.interfaces.InputDto;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.validators.ValidBrazilianPhone;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUsuarioInputDto implements InputDto<Usuario> {
    @NotBlank(message="Nome obrigatorio")
    private String nome;
    @NotBlank(message="Senha obrigatoria")
    private String senha;
    @NotBlank(message="Telefone obrigatório")
    @ValidBrazilianPhone
    private String telefone;

    @Override
    public Usuario toModel() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setSenha(this.senha);
        usuario.setTelefone(this.telefone);
        return usuario;
    }
}
