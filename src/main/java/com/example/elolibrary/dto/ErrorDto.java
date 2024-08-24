package com.example.elolibrary.dto;

import com.example.elolibrary.interfaces.Dto;
import lombok.Data;

@Data
public class ErrorDto implements Dto<String> {
    private String error;

    @Override
    public Dto<String> wrap(String error) {
        this.error = error;
        return this;
    }

    @Override
    public String unwrap() {
        return this.error;
    }
}
