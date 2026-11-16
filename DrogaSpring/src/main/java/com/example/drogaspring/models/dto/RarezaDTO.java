package com.example.drogaspring.models.dto;

import com.example.drogaspring.repositories.entities.RarezaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data //lombok nos crea los getters, setters y toString solos
@AllArgsConstructor //constructor con to do
@NoArgsConstructor //constructor vacio para poder hacer "new RarezaDTO()"
public class RarezaDTO {

    int id;
    String nombre;
    float porcentaje;
    int nivel;


    //hacemos el convert/dto/entity

    //de Entity a DTO -> traduce lo que sale del repositorio para mandarlo al servicio/controlador/html
    public static RarezaDTO convertToDTO(RarezaEntity entity){
        //si por lo que sea una droga no tiene rareza (es null), devolvemos un DTO vacio para que no pete el programa
        if (entity == null) return new RarezaDTO();

        RarezaDTO dto = new RarezaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setPorcentaje(entity.getPorcentaje());
        dto.setNivel(entity.getNivel());
        return dto;
    }

    //de DTO a Entity -> traduce lo que viene del controlador/servicio para guardarlo o buscarlo en el repositorio
    public static RarezaEntity convertToEntity(RarezaDTO dto){
        //igual que arriba, si nos llega vacio devolvemos una entity vacia para evitar el NullPointerException
        if (dto == null) return new RarezaEntity();

        RarezaEntity entity = new RarezaEntity();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setPorcentaje(dto.getPorcentaje());
        entity.setNivel(dto.getNivel());
        return entity;
    }

    //compara solo por ID (con == porque int es primitivo, no objeto como Long)
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RarezaDTO rarezaDTO = (RarezaDTO) o;
        return id == rarezaDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
CHULETA RAPIDA:
- ¿Por qué lleva "if (entity == null)" y DrogaDTO no? Porque como DrogaDTO tiene un RarezaDTO dentro, cuando creas una DrogaDTO nueva para buscar por ID (ej: en el delete o findById), su rareza va a null. Si no pones ese if, al intentar hacer entity.getId() salta un NullPointerException y peta.
- int vs Long en el equals -> en DrogaDTO el id es Long (objeto) y se compara con Objects.equals(id, drogaDTO.id). Aquí como pusiste "int id" (primitivo), se compara directo con "id == rarezaDTO.id" y cuando está vacío vale 0 en vez de null.
*/