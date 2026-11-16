package com.example.drogaspring.repositories.dao;

import com.example.drogaspring.repositories.entities.RarezaEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository //le dice a spring que este es el almacen de datos de las rarezas
public class RarezaRepositoryImpl implements RarezaRepositoy {

    //nuestra "base de datos" de rarezas en memoria
    private static List<RarezaEntity> datos = new ArrayList<>();

    @Override
    public List<RarezaEntity> findAll() {
        return datos; //devuelve toda la lista de rarezas
    }

    @Override
    public RarezaEntity findById(RarezaEntity rarezaEntity) {
        int posicion = datos.indexOf(rarezaEntity); //busca en que posicion esta la rareza con ese ID, si no esta devuelve -1

        if (posicion >= 0){ //si es 0 o mas esque la ha encontrado
            return datos.get(posicion); //sacamos la rareza entera de esa posicion y la devolvemos
        }
        return null; //si dio -1 esque no existe, devolvemos null
    }

    @Override
    public void save(RarezaEntity rarezaEntity) {
        //comprobamos si ya trae un ID (distinto de 0 porque es int) y si ya existe en la lista
        if(rarezaEntity.getId() != 0 && findById(rarezaEntity) != null){
            //EDITAR una rareza que ya existia
            int posicion = datos.indexOf(rarezaEntity); //vemos en que hueco esta
            datos.set(posicion, rarezaEntity); //machacamos ese hueco con los datos editados
        }else{
            //CREAR una rareza nueva
            int idNuevo;
            if (datos.isEmpty()){
                idNuevo = 1; //si la lista esta vacia, estrenamos con el ID 1
            }else{
                int ultimaPosicion = datos.size() - 1; //cogemos la posicion de la ultima rareza
                RarezaEntity ultimaRareza = datos.get(ultimaPosicion); //sacamos esa ultima rareza
                idNuevo = ultimaRareza.getId() + 1; //le sumamos 1 a su ID para no repetir nunca
            }
            rarezaEntity.setId(idNuevo); //le enchufamos el ID nuevo
            datos.add(rarezaEntity); //la guardamos al final de la lista
        }
    }

    @Override
    public void delete(RarezaEntity rarezaEntity) {
        //buscamos en que posicion esta la rareza que queremos borrar por su ID
        int posicion = datos.indexOf(rarezaEntity);

        //si la encuentra, la quita de la lista
        if (posicion >= 0){
            datos.remove(posicion);
        }
    }

    //bloque static: carga las 6 rarezas por defecto nada mas arrancar la app
    static {
        // 1. Común
        RarezaEntity r1 = new RarezaEntity();
        r1.setId(Integer.valueOf(datos.size() + 1)); //0 + 1 = ID 1
        r1.setNombre("COMUN");
        r1.setNivel(10);
        r1.setPorcentaje(50);
        datos.add(r1);

        // 2. Poco Común
        RarezaEntity r2 = new RarezaEntity();
        r2.setId(Integer.valueOf(datos.size() + 1)); //1 + 1 = ID 2
        r2.setNombre("POCO COMUN");
        r2.setNivel(25);
        r2.setPorcentaje(30);
        datos.add(r2);

        // 3. Raro
        RarezaEntity r3 = new RarezaEntity();
        r3.setId(Integer.valueOf(datos.size() + 1));
        r3.setNombre("RARO");
        r3.setNivel(50);
        r3.setPorcentaje(15);
        datos.add(r3);

        // 4. Épico (Tu base)
        RarezaEntity r4 = new RarezaEntity();
        r4.setId(Integer.valueOf(datos.size() + 1));
        r4.setNombre("EPICO");
        r4.setNivel(80);
        r4.setPorcentaje(2);
        datos.add(r4);

        // 5. Legendario
        RarezaEntity r5 = new RarezaEntity();
        r5.setId(Integer.valueOf(datos.size() + 1));
        r5.setNombre("LEGENDARIO");
        r5.setNivel(95);
        r5.setPorcentaje(1);
        datos.add(r5);

        // 6. Mítico
        RarezaEntity r6 = new RarezaEntity();
        r6.setId(Integer.valueOf(datos.size() + 1));
        r6.setNombre("MITICO");
        r6.setNivel(100);
        r6.setPorcentaje(1);
        datos.add(r6);
    }
}

/*
CHULETA RAPIDA:
- != 0 vs != null en el save() -> en DrogaEntity el id es Long (objeto), así que cuando viene vacío del formulario de crear vale "null". En RarezaEntity el id es "int" (primitivo), y los int en Java nunca pueden ser null, su valor por defecto cuando están vacíos es 0. Por eso aquí comprobamos "rarezaEntity.getId() != 0".
- ¿Por qué "datos" tiene que ser static? Porque en DrogaRepositoryImpl hiciste "new RarezaRepositoryImpl()" dentro de su bloque static para pillar las rarezas 1, 2 y 3. Al ser "static List<RarezaEntity> datos", da igual cuántos "new" hagas, todos leen exactamente la misma lista de rarezas.
*/