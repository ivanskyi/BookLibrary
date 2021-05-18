package com.example.booklibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessBookRateDto implements Comparable<SuccessBookRateDto> {
    private double rate;

    @Override
    public int compareTo(SuccessBookRateDto object) {
        int result = 0;
        if (this.getRate() == object.getRate()) result = 0;
        if (this.getRate() > object.getRate()) result = 1;
        if (this.getRate() < object.getRate()) result = -1;
        return result;
    }
}
