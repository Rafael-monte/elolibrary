package com.example.elolibrary.controller;


import com.example.elolibrary.interfaces.CRUDController;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController implements CRUDController<Usuario> {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public ResponseEntity<?> findAllByPage(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<?> save(Usuario usuario) {
        return null;
    }

    @Override
    public ResponseEntity<?> update(Long id, Usuario usuario) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        return null;
    }
}
