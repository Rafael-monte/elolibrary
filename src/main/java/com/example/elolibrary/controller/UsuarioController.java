package com.example.elolibrary.controller;


import com.example.elolibrary.dto.output.ErrorOutputDto;
import com.example.elolibrary.dto.input.UpdateUsuarioInputDto;
import com.example.elolibrary.dto.input.UsuarioInputDto;
import com.example.elolibrary.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> findAllByPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "50") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(this.usuarioService.findAllByPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.usuarioService.findById(id));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @PostMapping()
    public ResponseEntity<?> save(@Valid @RequestBody UsuarioInputDto usuarioInputDto) {
        try {
            this.usuarioService.save(usuarioInputDto.toModel());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateUsuarioInputDto updateUsuarioInputDto) {
        try {
            return ResponseEntity.ok(this.usuarioService.update(updateUsuarioInputDto.toModel(), id));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Itera pelos erros de validação e os adiciona ao mapa de erros
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Retorna um mapa de erros com o status 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
