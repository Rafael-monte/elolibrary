package com.example.elolibrary.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CRUDController<Model> {

    @GetMapping()
    ResponseEntity<?> findAllByPage(Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<?> findById(@PathVariable Long id);

    @PostMapping()
    ResponseEntity<?> save(@RequestBody Model model);

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody Model model);

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id);

}
