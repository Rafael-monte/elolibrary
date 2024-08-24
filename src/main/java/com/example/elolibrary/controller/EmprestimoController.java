package com.example.elolibrary.controller;

import com.example.elolibrary.dto.EmprestimoInputDto;
import com.example.elolibrary.dto.ErrorDto;
import com.example.elolibrary.model.Emprestimo;
import com.example.elolibrary.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<?> createNewEmprestimo(@RequestBody EmprestimoInputDto emprestimoInputDto) {
        try {
            this.emprestimoService.createEmprestimo(emprestimoInputDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorDto().wrap(e.getStatusText()));
        }
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateEmprestimo(@RequestBody Emprestimo emprestimo, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.emprestimoService.updateEmprestimo(emprestimo, id));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorDto().wrap(e.getStatusText()));
        }
    }

}
