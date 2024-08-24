package com.example.elolibrary.dto.input;

import com.example.elolibrary.interfaces.InputDto;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.validators.ValidISBN;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LivroInputDto implements InputDto<Livro> {

    @NotBlank(message="O título é obrigatório")
    private String titulo;
    @NotBlank(message="O autor é obrigatório")
    private String autor;
    @NotBlank(message = "O ISBN é obrigatório")
    @ValidISBN
    private String isbn;
    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;
    @NotBlank(message="A data de publicação é obrigatória")
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
