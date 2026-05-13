package com.arturoroes.minierp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponseDto {
    private Long id;
    private String name;
    private String taxId;
    private String email;
    private String phone;
    private String country;
}
