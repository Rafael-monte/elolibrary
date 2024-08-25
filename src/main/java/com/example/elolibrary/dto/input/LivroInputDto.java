package com.example.elolibrary.dto.input;

import com.example.elolibrary.interfaces.InputDto;
import com.example.elolibrary.model.Livro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDate;

@Data
public class LivroInputDto implements InputDto<Livro> {

    @NotBlank(message="Titulo obrigatorio")
    private String titulo;
    @NotBlank(message="Autor obrigatorio")
    private String autor;
    @NotBlank(message = "ISBN obrigatorio")
    @ISBN(message="ISBN invalido informado")
    private String isbn;
    @NotBlank(message = "Categoria obrigatoria")
    private String categoria;
    @NotNull(message="Data de publicacao obrigatoria")
    private LocalDate dataPublicacao;

    @Override
    public Livro toModel() {
        Livro livro = new Livro();
        livro.setTitulo(this.titulo);
        livro.setAutor(this.autor);
        livro.setIsbn(this.isbn);
        livro.setCategoria(this.categoria);
        livro.setDataPublicacao(this.dataPublicacao);
        return livro;
    }
}
