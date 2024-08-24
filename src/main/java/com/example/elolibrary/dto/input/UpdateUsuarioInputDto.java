package com.example.elolibrary.dto.input;

import com.example.elolibrary.interfaces.InputDto;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.validators.ValidBrazilianPhone;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUsuarioInputDto implements InputDto<Usuario> {
    @NotBlank(message="O nome é obrigatório")
    private String nome;
    @NotBlank(message="A senha é obrigatória")
    private String senha;
    @NotBlank(message="O telefone é obrigatório")
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
