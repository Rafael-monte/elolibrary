package com.example.elolibrary.controller;

import com.example.elolibrary.dto.input.EmprestimoInputDto;
import com.example.elolibrary.dto.input.EmprestimoUpdateInputDto;
import com.example.elolibrary.dto.output.ErrorOutputDto;
import com.example.elolibrary.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<?> createNewEmprestimo(@Valid @RequestBody EmprestimoInputDto emprestimoInputDto) {
        try {
            this.emprestimoService.createEmprestimo(emprestimoInputDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorOutputDto().wrap(e.getStatusText()));
        }
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateEmprestimo(@Valid @RequestBody EmprestimoUpdateInputDto emprestimoUpdateInputDto, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.emprestimoService.updateEmprestimo(emprestimoUpdateInputDto, id));
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
