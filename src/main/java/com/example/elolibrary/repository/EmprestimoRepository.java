package com.example.elolibrary.repository;

import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Optional<Emprestimo> findByUsuarioAndLivroAndStatusIn(Usuario usuario, Livro livro, List<StatusEmprestimo> status);
    Optional<Emprestimo> findByLivroAndStatusIn(Livro livro, List<StatusEmprestimo> status);
}
