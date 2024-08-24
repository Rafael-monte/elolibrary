package com.example.elolibrary.interfaces;

public interface Dto<Model> {
    Dto<Model> wrap(Model model);
    Model unwrap();
}
