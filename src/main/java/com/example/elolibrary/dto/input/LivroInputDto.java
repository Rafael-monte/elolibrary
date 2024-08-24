package com.example.elolibrary.dto.input;

import com.example.elolibrary.interfaces.InputDto;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.validators.ValidISBN;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LivroInputDto implements InputDto<Livro> {

    @NotBlank(message="Titulo obrigatorio")
    private String titulo;
    @NotBlank(message="Autor obrigatorio")
    private String autor;
    @NotBlank(message = "ISBN obrigatorio")
    @ValidISBN
    private String isbn;
    @NotBlank(message = "Categoria obrigatoria")
    private String categoria;
    @NotBlank(message="Data de publicacao obrigatoria")
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
