package com.example.drogaspring.services;

import com.example.drogaspring.models.dto.DrogaDTO;

import java.util.List;

//el contrato del servicio: igual que el repo, pero aqui ya todo entra y sale en formato DTO
public interface DrogaService {

    List<DrogaDTO> findAll();
    DrogaDTO findById(DrogaDTO drogaDTO);
    void save(DrogaDTO drogaDTO);
    void delete(DrogaDTO drogaDTO);
}