package com.example.drogaspring.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data //lombok nos hace los getters, setters y toString por detrás
@AllArgsConstructor //constructor con todos los atributos
@NoArgsConstructor //constructor vacio (obligatorio para poder hacer "new DrogaEntity()")
public class DrogaEntity {

    private Long id;
    private String nombre;
    private String tipo;
    private int duracionEfecto;
    private RarezaEntity rarezaEntity; //guarda la Entity de rareza, nunca el DTO (las entities solo se juntan con entities)

    //compara dos drogas solo por su ID (aqui es donde ocurre la magia cuando el repo hace datos.indexOf)
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DrogaEntity that = (DrogaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}