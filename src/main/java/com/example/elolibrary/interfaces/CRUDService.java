package com.example.elolibrary.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpClientErrorException;

public interface CRUDService<Model, OutputDto extends com.example.elolibrary.interfaces.OutputDto<Model>> {
    Page<com.example.elolibrary.interfaces.OutputDto<Model>> findAllByPage(Pageable pageable);
    com.example.elolibrary.interfaces.OutputDto<Model> findById(Long id) throws HttpClientErrorException.NotFound;
    void save(Model model) throws HttpClientErrorException.BadRequest;
    com.example.elolibrary.interfaces.OutputDto<Model> update(Model model, Long id) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound;
    void deleteById(Long id) throws HttpClientErrorException.NotFound;
}
