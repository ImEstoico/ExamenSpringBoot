package com.example.drogaspring.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data //getters, setters y toString automaticos
@AllArgsConstructor
@NoArgsConstructor
public class RarezaEntity {

    int id;
    String nombre;
    float porcentaje;
    int nivel;

    //igual que en DrogaEntity: si dos rarezas tienen el mismo ID, para Java son la misma
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RarezaEntity that = (RarezaEntity) o;
        return id == that.id; //con == porque aqui el id es int (primitivo)
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
CHULETA RAPIDA:
- Entity vs DTO -> La Entity representa el dato "puro" de la base de datos (solo vive en el Repository y entra al Service para traducirse). No tiene metodos convertTo... porque las traducciones ya las escribimos todas dentro de los DTOs.
- Peligro con @Data de Lombok -> @Data te crea un equals() y hashCode() por defecto que compara TODOS los atributos (nombre, tipo, duracion...). Por eso escribimos nosotros a mano el @Override de equals y hashCode abajo del todo: para machacar el de Lombok y obligarle a que compare SOLO por el ID. Si se te olvida poner el equals por ID en la Entity, el findById, el update y el delete del repositorio dejan de funcionar (dan siempre -1).
*/