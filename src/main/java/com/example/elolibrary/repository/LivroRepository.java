package com.example.elolibrary.repository;

import com.example.elolibrary.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LivroRepository extends JpaRepository<Livro, Long>, PagingAndSortingRepository<Livro, Long> {
}
