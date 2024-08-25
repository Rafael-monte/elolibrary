package com.example.elolibrary.repository;

import com.example.elolibrary.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, PagingAndSortingRepository<Usuario, Long> {
    List<Usuario> findAllByAtivoTrue(Pageable pageable);
    Optional<Usuario> findByEmailAndAtivoTrue(String email);
    Optional<Usuario> findByTelefoneAndAtivoTrue(String telefone);
    void deleteByAtivoFalse();
}
