package com.example.drogaspring.services;

import com.example.drogaspring.models.dto.RarezaDTO;
import com.example.drogaspring.repositories.dao.RarezaRepositoy;
import com.example.drogaspring.repositories.entities.RarezaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service //le decimos a spring que este es el traductor de las rarezas
public class RarezaServiceImpl implements RarezaService {

    //@Autowired conecta con el repositorio de rarezas automaticamente
    @Autowired
    private RarezaRepositoy rarezaRepositoy;

    @Override
    public List<RarezaDTO> findAll() {
        List<RarezaEntity> listaEntities = rarezaRepositoy.findAll(); //pedimos al repo la lista de entities
        List<RarezaDTO> listaDTOs = new ArrayList<>(); //lista vacia para meter las rarezas traducidas

        for (int i = 0; i < listaEntities.size(); i++) {
            RarezaEntity entity = listaEntities.get(i);

            RarezaDTO dto = RarezaDTO.convertToDTO(entity); //traducimos cada entity a dto

            listaDTOs.add(dto);
        }

        return listaDTOs; //mandamos la lista traducida al controlador
    }

    @Override
    public RarezaDTO findById(RarezaDTO rarezaDTO) {
        //recibimos un dto con solo el ID y lo pasamos a entity para buscar en el repo
        RarezaEntity rarezaBuscar = RarezaDTO.convertToEntity(rarezaDTO);
        RarezaEntity rarezaEncontrada = rarezaRepositoy.findById(rarezaBuscar);

        if (rarezaEncontrada != null) { //si el repo la encuentra, la volvemos a traducir a DTO y la devolvemos
            return RarezaDTO.convertToDTO(rarezaEncontrada);
        }
        return null; //si no existe, devolvemos null
    }

    @Override
    public void save(RarezaDTO rarezaDTO) {
        //traducimos de DTO a Entity y guardamos en el repo
        RarezaEntity entity = RarezaDTO.convertToEntity(rarezaDTO);
        rarezaRepositoy.save(entity);
    }

    @Override
    public void delete(RarezaDTO rarezaDTO) {
        //traducimos de DTO a Entity y borramos en el repo
        RarezaEntity entity = RarezaDTO.convertToEntity(rarezaDTO);
        rarezaRepositoy.delete(entity);
    }
}

/*
CHULETA RAPIDA:
- El Service es un "puente/traductor": el Controller solo habla idioma DTO y el Repository solo habla idioma Entity. El Service está en medio convirtiendo de uno a otro con convertToDTO() y convertToEntity().
- Regla de oro para no liarse:
  1. Cuando el dato VA hacia la base de datos (save, delete, o el parámetro de entrada de findById) -> convertToEntity().
  2. Cuando el dato VIENE de la base de datos hacia la web (el return de findAll y el return de findById) -> convertToDTO().
- Ojo con @Service: solo hace falta ponerlo en la clase Impl (DrogaServiceImpl y RarezaSericeImpl), en la interfaz no es necesario (aunque si lo pones no peta).
*/