package com.example.drogaspring.repositories.dao;

import com.example.drogaspring.repositories.entities.DrogaEntity;
import com.example.drogaspring.repositories.entities.RarezaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository //le dice a spring que esta clase es el almacen de datos (simula la base de datos)
public class DrogaRepositoryImpl implements DrogaRepository{

    @Autowired
    private RarezaRepositoy rarezaRepositoy;

    //nuestra "base de datos" en memoria, static para que sea una sola lista compartida
    private static List<DrogaEntity> datos = new ArrayList<>();

    @Override
    public List<DrogaEntity> findAll() {
        return datos; //devuelve to-do #datos
    }

    @Override
    public DrogaEntity findById(DrogaEntity drogaEntity) {

        int posicion = datos.indexOf(drogaEntity); //busca cual es la posicion del dato en el array d datos, si no existe = -1

        if (posicion >= 0 ){ //si es mayor o igual a 0 esque ha encontrado algo
            return datos.get(posicion); //pillamos la droga entera y la devolvemos
        }
        return null; //esto es que no ha encontrado nada (es -1)
    }

    @Override
    public void save(DrogaEntity drogaEntity) {
        //aqui vamos a comprobar si tiene un ID y si ya existe en nuestra lista
        if (drogaEntity.getId() != null && findById(drogaEntity) != null){
            //aqui es la edicion

            int posicion = datos.indexOf(drogaEntity); //buscamos en q posicion está
            datos.set(posicion, drogaEntity); //machaca la posicion con los datos nuevos

        }else{
            //crear nueva INSERTAR
            Long idNuevo; //creamos el nuevo id
            if (datos.isEmpty()){
                idNuevo = 1L; //si datos esta limpio que empiece por el ID 1
            }else {
                int ultimaPosicion = datos.size() - 1; //miramos cual es el ultimo hueco de la lista
                DrogaEntity ultimaDroga = datos.get(ultimaPosicion); //pillamos esa ultima droga
                idNuevo = ultimaDroga.getId() + 1; //le sumamos 1 a su id para que no se repita aunque hayamos borrado alguna
            }
            drogaEntity.setId(idNuevo); //le ponemos el id calculado
            datos.add(drogaEntity); //la metemos en la lista

        }

    }

    @Override
    public void delete(DrogaEntity drogaEntity) {

        //se busca en q posicion esta la q queremos borrar con id
        int posicion = datos.indexOf(drogaEntity);

        //si la encuentra
        if(posicion >= 0){
            datos.remove(posicion);
        }

    }

    //bloque static: se ejecuta una sola vez al arrancar el programa para meter datos de prueba
    static {
        //instanciamos el repo de rarezas a mano porque dentro de un static no funciona el @Autowired
        RarezaRepositoy rr = new RarezaRepositoryImpl();

        //reutilizamos el objeto "id" cambiando el numero para sacar las rarezas 1, 2 y 3 del otro repo
        RarezaEntity id = new RarezaEntity();
        id.setId(1);
        RarezaEntity r1 = rr.findById(id);

        id.setId(2);
        RarezaEntity r2 = rr.findById(id);

        id.setId(3);
        RarezaEntity r3 = rr.findById(id);


        DrogaEntity d1 = new DrogaEntity();
        d1.setId(Long.valueOf(datos.size() + 1)); //calcula el id segun cuantas haya metidas (0 + 1 = 1)
        d1.setNombre("marijuana");
        d1.setTipo("hierba");
        d1.setDuracionEfecto(230);
        d1.setRarezaEntity(r1); //le enchufamos la rareza 1 (COMUN)
        datos.add(d1);

        DrogaEntity d2 = new DrogaEntity();
        d2.setId(Long.valueOf(datos.size() + 1));
        d2.setNombre("coca");
        d2.setTipo("Javier");
        d2.setDuracionEfecto(400);
        d2.setRarezaEntity(r2); //rareza 2 (POCO COMUN)
        datos.add(d2);

        DrogaEntity d3 = new DrogaEntity();
        d3.setId(Long.valueOf(datos.size() + 1));
        d3.setNombre("fentanilo");
        d3.setTipo("pinchable");
        d3.setDuracionEfecto(73);
        d3.setRarezaEntity(r3); //rareza 3 (RARO)
        datos.add(d3);

        DrogaEntity d5 = new DrogaEntity();
        d5.setId(Long.valueOf(datos.size() + 1));
        d5.setNombre("amor");
        d5.setTipo("El Peor");
        d5.setDuracionEfecto(900);
        d5.setRarezaEntity(r1);
        datos.add(d5);
    }
}

/*
CHULETA RAPIDA:
- ¿Cómo hace magia datos.indexOf(drogaEntity)? indexOf busca en la lista usando el metodo equals() de DrogaEntity. Como pusimos que el equals solo mire el ID, aunque le pases una droga vacia que solo tiene puesto el ID (como hacemos en delete o findById), encuentra la posicion de la droga completa en la lista.
- datos.set(posicion, objeto) vs datos.add(objeto) -> set() sustituye lo que habia en esa posicion (EDITAR), add() lo añade al final de la lista (CREAR).
- ¿Por qué cogemos ultimaDroga.getId() + 1 en el save en vez de datos.size() + 1? Porque si tienes 4 drogas (IDs 1, 2, 3, 4) y borras la 2, el size() pasa a ser 3. Si hicieras size() + 1, la nueva tendria el ID 4 (repetido con la ultima). Mirando el ID de la ultima (4 + 1 = 5) nunca se pisan.
*/