package com.example.elolibrary.controller;


import com.example.elolibrary.dto.ErrorDto;
import com.example.elolibrary.model.Livro;
import com.example.elolibrary.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorDto().wrap(e.getStatusText()));
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Livro livro) {
        try {
            this.livroService.save(livro);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorDto().wrap(e.getStatusText()));
        }
    }

    @PutMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Livro livro) {
        try {
            return ResponseEntity.ok(this.livroService.update(livro, id));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorDto().wrap(e.getStatusText()));
        }
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.livroService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorDto().wrap(e.getStatusText()));
        }
    }
}
