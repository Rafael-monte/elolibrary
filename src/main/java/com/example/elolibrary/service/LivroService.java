package com.example.elolibrary.service;

import com.example.elolibrary.dto.output.LivroOutputDto;
import com.example.elolibrary.interfaces.CRUDService;
import com.example.elolibrary.interfaces.OutputDto;
import com.example.elolibrary.model.Livro;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService implements CRUDService<Livro, OutputDto<Livro>> {

    @Autowired
    private LivroRepository livroRepository;

    private static final String ENTIDADE_SERVICE="Livro";


    @Override
    public Page<OutputDto<Livro>> findAllByPage(Pageable pageable) {
        List<OutputDto<Livro>> pageLivro = livroRepository.findAllByAtivoTrue(pageable)
                .stream()
                .map(livro -> new LivroOutputDto().wrap(livro))
                .toList();
        return new PageImpl<>(pageLivro, pageable, pageLivro.size());
    }

    @Override
    public OutputDto<Livro> findById(Long id) throws HttpClientErrorException.NotFound {
        Optional<Livro> optLivro = this.livroRepository.findById(id);
        if (optLivro.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND, ENTIDADE_SERVICE, id.toString()
                    )
            );
        }
        return new LivroOutputDto().wrap(optLivro.get());
    }

    @Override
    public void save(Livro livro) throws HttpClientErrorException.BadRequest {
        this.checkIfISBNIsBeingUsed(livro.getIsbn(), null);
        this.checkDataPublicacao(livro.getDataPublicacao());
        livro.setAtivo(true);
        this.livroRepository.saveAndFlush(livro);
    }

    @Override
    public OutputDto<Livro> update(Livro livro, Long id) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound {
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
        return new LivroOutputDto().wrap(livro);
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
        livro.setIsbn(null);
        this.livroRepository.saveAndFlush(livro);
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
        Optional<Livro> optLivro = this.livroRepository.findByIsbnAndAtivoTrue(isbn);
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
