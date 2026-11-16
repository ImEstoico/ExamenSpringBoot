package com.example.drogaspring.repositories.dao;

import com.example.drogaspring.repositories.entities.RarezaEntity;

import java.util.List;

//el contrato del repo de rarezas: solo declaramos los metodos que van a manejar las RarezaEntity
public interface RarezaRepositoy {
    List<RarezaEntity> findAll();
    RarezaEntity findById(RarezaEntity rarezaEntity);
    void save(RarezaEntity rarezaEntity);
    void delete(RarezaEntity rarezaEntity);
}