package com.example.elolibrary.interfaces;

public interface OutputDto<Model> {
    OutputDto<Model> wrap(Model model);
    Model unwrap();
}
