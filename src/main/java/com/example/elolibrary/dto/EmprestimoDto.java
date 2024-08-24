package com.example.elolibrary.dto;

import com.example.elolibrary.interfaces.Dto;
import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoDto implements Dto<Emprestimo> {

    private Long id;
    private UsuarioDto usuario;
    private LivroDto livro;
    private LocalDate dataDevolucao;
    private LocalDate dataEmprestimo;
    private StatusEmprestimo status;



    @Override
    public Dto<Emprestimo> wrap(Emprestimo emprestimo) {
        this.id = emprestimo.getId();
        this.usuario = (UsuarioDto) new UsuarioDto().wrap(emprestimo.getUsuario());
        this.livro = (LivroDto) new LivroDto().wrap(emprestimo.getLivro());
        this.dataDevolucao = emprestimo.getDataDevolucao();
        this.dataEmprestimo = emprestimo.getDataEmprestimo();
        this.status = emprestimo.getStatus();
        return this;
    }

    @Override
    public Emprestimo unwrap() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(this.id);
        emprestimo.setDataEmprestimo(this.dataEmprestimo);
        emprestimo.setDataDevolucao(this.dataDevolucao);
        emprestimo.setUsuario(this.usuario.unwrap());
        emprestimo.setLivro(this.livro.unwrap());
        emprestimo.setStatus(this.getStatus());
        return emprestimo;
    }
}
