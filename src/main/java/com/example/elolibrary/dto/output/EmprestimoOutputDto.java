package com.example.elolibrary.dto.output;

import com.example.elolibrary.interfaces.OutputDto;
import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoOutputDto implements OutputDto<Emprestimo> {

    private Long id;
    private UsuarioOutputDto usuario;
    private LivroOutputDto livro;
    private LocalDate dataDevolucao;
    private LocalDate dataEmprestimo;
    private StatusEmprestimo status;



    @Override
    public OutputDto<Emprestimo> wrap(Emprestimo emprestimo) {
        this.id = emprestimo.getId();
        this.usuario = (UsuarioOutputDto) new UsuarioOutputDto().wrap(emprestimo.getUsuario());
        this.livro = (LivroOutputDto) new LivroOutputDto().wrap(emprestimo.getLivro());
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
