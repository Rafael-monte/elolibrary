package com.example.elolibrary.controller;


import com.example.elolibrary.dto.output.ErrorOutputDto;
import com.example.elolibrary.dto.input.LivroInputDto;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.service.LivroService;
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
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<?> findAllByPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "50") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(this.livroService.findAllByPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.livroService.findById(id));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody LivroInputDto livroInputDto) {
        try {
            this.livroService.save(livroInputDto.toModel());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @PutMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Livro livro) {
        try {
            return ResponseEntity.ok(this.livroService.update(livro, id));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.livroService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
