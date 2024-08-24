package com.example.elolibrary.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

public interface CRUDService<Model> {
    List<Model> findAllByPage(Pageable pageable);
    Model findById(Long id) throws HttpClientErrorException.NotFound;
    void save(Model model) throws HttpClientErrorException.BadRequest;
    Model update(Model model, Long id) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound;
    void deleteById(Long id) throws HttpClientErrorException.NotFound;
}
