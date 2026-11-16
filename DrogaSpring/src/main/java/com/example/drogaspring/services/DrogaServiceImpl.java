package com.example.drogaspring.services;

import com.example.drogaspring.models.dto.DrogaDTO;
import com.example.drogaspring.repositories.dao.DrogaRepository;
import com.example.drogaspring.repositories.entities.DrogaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service //con esto le decimos a spring que esta es la clase que hace de traductor
public class DrogaServiceImpl implements DrogaService {

    //@Autowired conecta el servicio con el repositorio sin hacer un "new DrogaRepositoryImpl()"
    @Autowired
    private DrogaRepository drogaRepository;

    @Override
    public List<DrogaDTO> findAll() {
        List<DrogaEntity> listaEntities = drogaRepository.findAll(); //le pedimos al repo la lista original de entities
        List<DrogaDTO> listaDTOs = new ArrayList<>(); //creamos la nueva lista donde se guardará to do traducido

        for (int i = 0; i < listaEntities.size(); i++) {
            DrogaEntity entity = listaEntities.get(i);

            DrogaDTO dto = DrogaDTO.convertToDTO(entity); //traducimos to do de la lista original a dto

            listaDTOs.add(dto);
        }

        return listaDTOs; //devolvemos la lista al controlador
    }

    @Override
    public DrogaDTO findById(DrogaDTO drogaDTO) {
        //el controlador pasa un dto que solo lleva una ID
        DrogaEntity drogaBuscar = DrogaDTO.convertToEntity(drogaDTO); //lo pasamos a entity para que el repo lo entienda
        DrogaEntity drogaEncontrada = drogaRepository.findById(drogaBuscar); //le pedimos a drogarepo que busque

        if (drogaEncontrada != null){ //vemos si ha encontrado algo y lo devuelve traducido a DTO al controlador
            return DrogaDTO.convertToDTO(drogaEncontrada);
        }
        return null; //sino pues null
    }

    @Override
    public void save(DrogaDTO drogaDTO) {
        //traducimos el DTO que viene del controlador a Entity y se lo damos al repo para que lo guarde
        DrogaEntity entity = DrogaDTO.convertToEntity(drogaDTO);
        drogaRepository.save(entity);
    }

    @Override
    public void delete(DrogaDTO drogaDTO) {
        //traducimos el DTO (que solo trae el ID) a Entity y le decimos al repo que lo borre
        DrogaEntity entity = DrogaDTO.convertToEntity(drogaDTO);
        drogaRepository.delete(entity);
    }
}