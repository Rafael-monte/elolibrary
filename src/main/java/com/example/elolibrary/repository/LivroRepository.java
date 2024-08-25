package com.example.elolibrary.repository;

import com.example.elolibrary.model.Livro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>, PagingAndSortingRepository<Livro, Long> {
    List<Livro> findAllByAtivoTrue(Pageable pageable);

    Optional<Livro> findByIsbnAndAtivoTrue(String isbn);

    void deleteByAtivoFalse();
}
