package com.example.elolibrary.dto;

import com.example.elolibrary.interfaces.Dto;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.util.DateUtils;
import com.example.elolibrary.util.ServiceUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class LivroDto implements Dto<Livro> {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private LocalDate dataPublicacao;
    private String categoria;

    @Override
    public Dto<Livro> wrap(Livro livro) {
        this.autor = livro.getAutor();
        this.id = livro.getId();
        this.titulo = livro.getTitulo();
        this.isbn = livro.getIsbn();
        this.dataPublicacao = livro.getDataPublicacao();
        this.categoria = livro.getCategoria();
        return this;
    }

    @Override
    public Livro unwrap() {
        Livro livro = new Livro();
        livro.setId(this.id);
        livro.setTitulo(this.titulo);
        livro.setAutor(this.autor);
        livro.setIsbn(this.isbn);
        livro.setCategoria(this.categoria);
        livro.setDataPublicacao(this.getDataPublicacao());
        return livro;
    }

}
