package com.example.elolibrary.service;

import com.example.elolibrary.dto.input.EmprestimoUpdateInputDto;
import com.example.elolibrary.dto.output.EmprestimoOutputDto;
import com.example.elolibrary.dto.input.EmprestimoInputDto;
import com.example.elolibrary.interfaces.OutputDto;
import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import com.example.elolibrary.repository.EmprestimoRepository;
import com.example.elolibrary.repository.LivroRepository;
import com.example.elolibrary.repository.UsuarioRepository;
import com.example.elolibrary.util.DateUtils;
import com.example.elolibrary.util.MessageTemplate;
import com.example.elolibrary.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;
    private UsuarioRepository usuarioRepository;
    private LivroRepository livroRepository;

    private static final String ENTIDADE_SERVICE="Emprestimo";

    public void createEmprestimo(EmprestimoInputDto emprestimoInputDto) throws HttpClientErrorException.BadRequest {
        this.checkDataDevolucao(emprestimoInputDto.getDataDevolucao());
        Optional<Usuario> optUsuario = this.usuarioRepository.findByEmail(emprestimoInputDto.getEmailUsuario());
        Optional<Livro> optLivro = this.livroRepository.findByIsbn(emprestimoInputDto.getIsbnLivro());
        if (optUsuario.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.EMAIL_NOT_FOUND,
                            emprestimoInputDto.getEmailUsuario()
                    )
            );
        }
        if (optLivro.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ISBN_NOT_FOUND,
                            emprestimoInputDto.getIsbnLivro()
                    )
            );
        }

        Usuario usuario = optUsuario.get();
        Livro livro = optLivro.get();
        this.checkIfTheresAnOpenEmprestimo(livro);
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.EM_USO);
        emprestimo.setDataDevolucao(emprestimoInputDto.getDataDevolucao());
        this.emprestimoRepository.saveAndFlush(emprestimo);
    }


    public OutputDto<Emprestimo> updateEmprestimo(EmprestimoUpdateInputDto emprestimo, Long emprestimoId) throws HttpClientErrorException.NotFound, HttpClientErrorException.BadRequest {
        Optional<Emprestimo> optEmprestimo = this.emprestimoRepository.findById(emprestimoId);
        if (optEmprestimo.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND,
                            ENTIDADE_SERVICE,
                            emprestimoId.toString()
                    )
            );
        }
        this.checkDataDevolucao(emprestimo.getDataDevolucao());
        this.checkDataEmprestimo(emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());
        Emprestimo emprestimoDaBase = optEmprestimo.get();
        emprestimoDaBase.setDataEmprestimo(emprestimo.getDataEmprestimo());
        emprestimoDaBase.setDataDevolucao(emprestimo.getDataDevolucao());
        emprestimoDaBase.setStatus(emprestimo.getStatus());
        this.emprestimoRepository.saveAndFlush(emprestimoDaBase);
        return new EmprestimoOutputDto().wrap(emprestimoDaBase);
    }


    private void markEmprestimoAsVencido(Emprestimo emprestimo) {
        emprestimo.setStatus(StatusEmprestimo.VENCIDO);
        this.emprestimoRepository.saveAndFlush(emprestimo);
    }

    private void checkIfTheresAnOpenEmprestimo(Livro livroEmprestimo) throws HttpClientErrorException.BadRequest {
        Optional<Emprestimo> optEmprestimo = this.emprestimoRepository.findByLivroAndStatusIn(
                livroEmprestimo,
                List.of(
                        StatusEmprestimo.EM_USO,
                        StatusEmprestimo.VENCIDO
                )
        );
        if (optEmprestimo.isPresent()) {
            Emprestimo emprestimoJaFeito = optEmprestimo.get();
            if (emprestimoJaFeito.getDataDevolucao().isAfter(LocalDate.now())) {
                this.markEmprestimoAsVencido(emprestimoJaFeito);
            }
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.BOOK_LOAN_ALREADY_EXISTS,
                            DateUtils.dateToDDMMYYYY(emprestimoJaFeito.getDataDevolucao()),
                            emprestimoJaFeito.getStatus().equals(StatusEmprestimo.VENCIDO)?
                                    "- Vencido":
                                    ""

                    )
            );
        }
    }

    private void checkDataDevolucao(LocalDate dataDevolucao) throws HttpClientErrorException.BadRequest {
        if (dataDevolucao.isBefore(LocalDate.now())) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.DEVOLUTION_DATE_BEFORE_CURRENT_DATE,
                            DateUtils.dateToDDMMYYYY(dataDevolucao)
                    )
            );
        }
    }

    private void checkDataEmprestimo(LocalDate dataEmprestimo, LocalDate dataDevolucao) throws HttpClientErrorException.BadRequest {
        if (dataEmprestimo.isBefore(LocalDate.now())) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.LOAN_DATE_AFTER_CURRENT_DATE,
                            DateUtils.dateToDDMMYYYY(dataEmprestimo)
                    )
            );
        }

        if (dataEmprestimo.isBefore(LocalDate.now())) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.LOAN_DATE_AFTER_DEVOLUTION_DATE,
                            DateUtils.dateToDDMMYYYY(dataEmprestimo),
                            DateUtils.dateToDDMMYYYY(dataDevolucao)
                    )
            );
        }
    }

}
