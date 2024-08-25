package com.example.elolibrary.dto.output;

import com.example.elolibrary.interfaces.OutputDto;
import lombok.Data;

@Data
public class ErrorOutputDto implements OutputDto<String> {
    private String error;

    @Override
    public OutputDto<String> wrap(String error) {
        this.error = error;
        return this;
    }

    @Override
    public String unwrap() {
        return this.error;
    }
}
