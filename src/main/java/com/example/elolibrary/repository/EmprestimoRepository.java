package com.example.elolibrary.repository;

import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.model.enumeration.StatusEmprestimo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long>, PagingAndSortingRepository<Emprestimo, Long> {
    Optional<Emprestimo> findByLivroAndStatusIn(Livro livro, List<StatusEmprestimo> status);
    List<Emprestimo> findByDataDevolucaoBeforeAndStatus(LocalDate dataDevolucao, StatusEmprestimo statusEmprestimo);
    void deleteByStatus(StatusEmprestimo statusEmprestimo);
    List<Emprestimo> findAllByStatusNot(StatusEmprestimo statusEmprestimo, Pageable pageable);

}
