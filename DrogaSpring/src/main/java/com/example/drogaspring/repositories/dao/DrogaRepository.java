package com.example.drogaspring.repositories.dao;

import com.example.drogaspring.repositories.entities.DrogaEntity;

import java.util.List;

//el contrato: aqui solo decimos QUE metodos existen, sin codigo dentro (trabaja siempre con Entities)
public interface DrogaRepository {

    List<DrogaEntity> findAll();
    DrogaEntity findById(DrogaEntity drogaEntity);
    void save(DrogaEntity drogaEntity);
    void delete(DrogaEntity drogaEntity);
}