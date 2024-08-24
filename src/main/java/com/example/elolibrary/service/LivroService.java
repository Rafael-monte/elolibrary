package com.example.elolibrary.service;

import com.example.elolibrary.dto.LivroDto;
import com.example.elolibrary.dto.UsuarioDto;
import com.example.elolibrary.interfaces.CRUDService;
import com.example.elolibrary.interfaces.Dto;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.Role;
import com.example.elolibrary.repository.LivroRepository;
import com.example.elolibrary.util.DateUtils;
import com.example.elolibrary.util.MessageTemplate;
import com.example.elolibrary.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class LivroService implements CRUDService<Livro, Dto<Livro>> {

    @Autowired
    private LivroRepository livroRepository;

    private static final String ENTIDADE_SERVICE="Livro";


    @Override
    public Page<Dto<Livro>> findAllByPage(Pageable pageable) {
        List<Dto<Livro>> pageLivro = livroRepository.findAllByAtivoTrue(pageable)
                .stream()
                .map(livro -> new LivroDto().wrap(livro))
                .toList();
        return new PageImpl<>(pageLivro, pageable, pageLivro.size());
    }

    @Override
    public Dto<Livro> findById(Long id) throws HttpClientErrorException.NotFound {
        Optional<Livro> optLivro = this.livroRepository.findById(id);
        if (optLivro.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND, ENTIDADE_SERVICE, id.toString()
                    )
            );
        }
        return new LivroDto().wrap(optLivro.get());
    }

    @Override
    public void save(Livro livro) throws HttpClientErrorException.BadRequest {
        this.checkIfFieldsAreNull(livro);
        this.checkIsbn(livro.getIsbn());
        this.checkIfISBNIsBeingUsed(livro.getIsbn(), null);
        this.checkDataPublicacao(livro.getDataPublicacao());
        livro.setAtivo(true);
        this.livroRepository.saveAndFlush(livro);
    }

    @Override
    public Dto<Livro> update(Livro livro, Long id) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound {
        this.checkIfFieldsAreNull(livro);
        this.checkIsbn(livro.getIsbn());
        this.checkDataPublicacao(livro.getDataPublicacao());
        Optional<Livro> optLivro = this.livroRepository.findById(id);
        if (optLivro.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND,
                            ENTIDADE_SERVICE,
                            id.toString()
                    )
            );
        }
        this.checkIfISBNIsBeingUsed(livro.getIsbn(), optLivro.get().getId());
        livro.setId(optLivro.get().getId());
        this.livroRepository.saveAndFlush(livro);
        return new LivroDto().wrap(livro);
    }

    @Override
    public void deleteById(Long id) throws HttpClientErrorException.NotFound {
        Optional<Livro> optLivro = this.livroRepository.findById(id);
        if (optLivro.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND,
                            ENTIDADE_SERVICE,
                            id.toString()
                    )
            );
        }
        Livro livro = optLivro.get();
        livro.setAtivo(false);
        this.livroRepository.saveAndFlush(livro);
    }

    private void checkIfFieldsAreNull(Livro livro)  {
        this.checkIfFieldIsEmpty(livro.getAutor(), "Autor");
        this.checkIfFieldIsEmpty(livro.getCategoria(), "Categoria");
        this.checkIfFieldIsEmpty(livro.getIsbn(), "ISBN");
        this.checkIfFieldIsEmpty(livro.getTitulo(), "Titulo");
    }

    private void checkIfFieldIsEmpty(String field, final String FIELD_NAME) throws HttpClientErrorException.BadRequest {
        if (ServiceUtils.isFalsyString(field)) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.NULL_FIELD,
                            FIELD_NAME
                    )
            );
        }
    }

    /*REFERENCIA: https://howtodoinjava.com/java/regex/java-regex-validate-international-standard-book-number-isbns/*/
    private void checkIsbn(String isbn) throws HttpClientErrorException.BadRequest {
        final String LIVRO_PATTERN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        Pattern pattern = Pattern.compile(LIVRO_PATTERN);
        if (!pattern.matcher(isbn).matches()) {
            throw new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                ServiceUtils.createExceptionMessage(MessageTemplate.INVALID_ISBN, isbn)
            );
        }
    }


    private void checkDataPublicacao(LocalDate dataPublicacao) throws HttpClientErrorException.BadRequest {
        if (dataPublicacao == null) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.NULL_FIELD,
                            "Data Publicação"
                    )
            );
        }
        if (dataPublicacao.isAfter(LocalDate.now())) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.REGISTER_DATE_AHEAD_OF_CURRENT_DATE,
                            DateUtils.dateToDDMMYYYY(dataPublicacao)
                    )
            );
        }
    }

    private void checkIfISBNIsBeingUsed(String isbn, Long livroId) throws HttpClientErrorException.BadRequest {
        Optional<Livro> optLivro = this.livroRepository.findByIsbn(isbn);
        if (optLivro.isPresent() && !optLivro.get().getId().equals(livroId)) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ISBN_ALREADY_IN_USE,
                            isbn
                    )
            );
        }
    }
}
