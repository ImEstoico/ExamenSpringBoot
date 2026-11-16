package com.example.drogaspring.services;

import com.example.drogaspring.models.dto.RarezaDTO;

import java.util.List;

//el contrato del servicio de rarezas (todo con RarezaDTO)
public interface RarezaService {

    List<RarezaDTO> findAll();
    RarezaDTO findById(RarezaDTO rarezaDTO);
    void save(RarezaDTO rarezaDTO);
    void delete(RarezaDTO rarezaDTO);
}