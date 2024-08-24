package com.example.elolibrary.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

public interface CRUDService<Model, OutputDto extends Dto<Model>> {
    Page<Dto<Model>> findAllByPage(Pageable pageable);
    Dto<Model> findById(Long id) throws HttpClientErrorException.NotFound;
    void save(Model model) throws HttpClientErrorException.BadRequest;
    Dto<Model> update(Model model, Long id) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound;
    void deleteById(Long id) throws HttpClientErrorException.NotFound;
}
