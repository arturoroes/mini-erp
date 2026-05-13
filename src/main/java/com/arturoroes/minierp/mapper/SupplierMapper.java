package com.arturoroes.minierp.mapper;

import com.arturoroes.minierp.dto.CategoryResponseDto;
import com.arturoroes.minierp.dto.SupplierRequestDto;
import com.arturoroes.minierp.dto.SupplierResponseDto;
import com.arturoroes.minierp.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierRequestDto dto){
        Supplier supplier= new Supplier();
        supplier.setName(dto.getName());
        supplier.setTaxId(dto.getTaxId());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setCountry(dto.getCountry());
        return supplier;
    }

    public void updateEntity(Supplier supplier, SupplierRequestDto dto){
        supplier.setName(dto.getName());
        supplier.setTaxId(dto.getTaxId());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setCountry(dto.getCountry());
    }

    public SupplierResponseDto toResponseDto(Supplier supplier){
        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getTaxId(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getCountry()
        );
    }
}
