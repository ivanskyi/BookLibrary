package com.example.booklibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessAuthorDto {
    private String authorName;
    private double rate;

    public SuccessAuthorDto(double rate) {
        this.rate = rate;
    }
}
