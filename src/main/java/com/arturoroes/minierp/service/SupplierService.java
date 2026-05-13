package com.arturoroes.minierp.service;

import com.arturoroes.minierp.dto.SupplierRequestDto;
import com.arturoroes.minierp.dto.SupplierResponseDto;
import com.arturoroes.minierp.entity.Supplier;
import com.arturoroes.minierp.exception.DuplicateResourceException;
import com.arturoroes.minierp.exception.ResourceNotFoundException;
import com.arturoroes.minierp.mapper.SupplierMapper;
import com.arturoroes.minierp.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper){
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Transactional(readOnly = true)
    public List<SupplierResponseDto> findAll(){
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SupplierResponseDto findById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
        return supplierMapper.toResponseDto(supplier);
    }

    public SupplierResponseDto create(SupplierRequestDto dto){
        if(dto.getTaxId() != null && !dto.getTaxId().isBlank() && supplierRepository.existsByTaxId(dto.getTaxId())){
                throw new DuplicateResourceException("Supplier", "taxID", dto.getTaxId());
        }
        Supplier supplier = supplierMapper.toEntity(dto);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponseDto(saved);
    }

    public SupplierResponseDto update(Long id, SupplierRequestDto dto){
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
        if (dto.getTaxId() != null && !dto.getTaxId().isBlank()
                && !dto.getTaxId().equals(supplier.getTaxId())
                && supplierRepository.existsByTaxId(dto.getTaxId())){
            throw new DuplicateResourceException("Supplier", "taxId", dto.getTaxId());
        }

        supplierMapper.updateEntity(supplier, dto);
        Supplier updated = supplierRepository.save(supplier);
        return supplierMapper.toResponseDto(updated);
    }

    public void delete(Long id){
        if(!supplierRepository.existsById(id)){
            throw new ResourceNotFoundException("Supplier", "id", id);
        }

        supplierRepository.deleteById(id);
    }
}
