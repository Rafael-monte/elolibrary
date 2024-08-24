package com.example.elolibrary.repository;

import com.example.elolibrary.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, PagingAndSortingRepository<Usuario, Long> {
    List<Usuario> findAllByAtivoTrue(Pageable pageable);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefone(String telefone);
}
