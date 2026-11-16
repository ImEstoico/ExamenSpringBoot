package com.example.drogaspring.models.dto;

import com.example.drogaspring.repositories.entities.DrogaEntity;
import com.example.drogaspring.repositories.entities.RarezaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data //lombok nos crea los getters, setters y toString sin escribirlos
@AllArgsConstructor //constructor con todos los datos
@NoArgsConstructor //constructor vacio (obligatorio para hacer "new DrogaDTO()")
public class DrogaDTO {

    private Long id;
    private String nombre;
    private String tipo;
    private int duracionEfecto;
    private RarezaDTO rarezaDTO; //en el DTO guardamos RarezaDTO, nunca la Entity


    //hacemos el convert/dto/entity

    //de Entity a DTO -> para sacar datos del repositorio y mandarlos al controlador/html
    public static DrogaDTO convertToDTO(DrogaEntity entity){
        DrogaDTO dto = new DrogaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setTipo(entity.getTipo());
        dto.setDuracionEfecto(entity.getDuracionEfecto());
        //como la entity tiene un RarezaEntity dentro, llamamos al conversor de RarezaDTO para traducirlo tambien
        dto.setRarezaDTO(RarezaDTO.convertToDTO(entity.getRarezaEntity()));
        return dto;
    }

    //de DTO a Entity -> para cuando recogemos del html/controlador y queremos guardar o buscar en el repositorio
    public static DrogaEntity convertToEntity(DrogaDTO dto){
        DrogaEntity entity = new DrogaEntity();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setTipo(dto.getTipo());
        entity.setDuracionEfecto(dto.getDuracionEfecto());
        //lo mismo al reves: traducimos el RarezaDTO a RarezaEntity para que el repo lo entienda
        entity.setRarezaEntity(RarezaDTO.convertToEntity(dto.getRarezaDTO()));

        return entity;
    }


    //el equals y hashCode solo con el ID para que dos drogas sean "la misma" si tienen el mismo ID
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DrogaDTO drogaDTO = (DrogaDTO) o;
        return Objects.equals(id, drogaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
CHULETA RAPIDA:
- ¿Para qué sirve el DTO? Es el "mensajero" que viaja entre el Service, el Controller y el HTML. La Entity nunca sale del Repository/Service.
- static en los conversores -> se pone static para poder llamarlos directamente con DrogaDTO.convertToDTO(...) sin tener que hacer un "new DrogaDTO()" antes.
- equals y hashCode por ID -> super importante (sobre to do en la Entity) para que cuando hagamos datos.indexOf() en el repositorio, encuentre el objeto aunque solo le hayamos metido el ID.
*/