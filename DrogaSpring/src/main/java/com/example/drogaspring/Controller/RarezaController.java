package com.example.drogaspring.Controller;

import com.example.drogaspring.models.dto.RarezaDTO;
import com.example.drogaspring.services.RarezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/rareza") //ruta base, todo lo de aqui dentro empieza por /rareza
public class RarezaController {

    @Autowired
    private RarezaService rarezaService;

    @GetMapping("")
    public ModelAndView findAll(){

        ModelAndView mav = new ModelAndView("rarezas"); //creamos la bandeja y decimos que abra rarezas.html

        List<RarezaDTO> listararezas = rarezaService.findAll(); //le pedimos al servicio la lista de rarezas en DTO

        //metemos la lista en la bandeja con la etiqueta listararezas
        //va entre comillas y es el que se usa en el html -> th:each="rareza : ${listararezas}"
        mav.addObject("listararezas", listararezas);

        return mav; //devolvemos la bandeja al navegador
    }

    @GetMapping("/update/{idRareza}")
    public ModelAndView editar(@PathVariable("idRareza") int idRareza){ //el pathvariable pilla el ${idRareza} de la url (aqui es int porque en Rareza el id es int)

        ModelAndView mav = new ModelAndView("formularioRareza"); //abrimos el formulario de rarezas

        RarezaDTO id = new RarezaDTO();
        id.setId(idRareza); //metemos el id en un dto vacio para buscarlo

        RarezaDTO rareza = rarezaService.findById(id); //buscamos la rareza entera en el servicio

        mav.addObject("rareza", rareza); //mandamos al html (NOMBRE ETIQUETA), (OBJETO)

        return mav;
    }

    @GetMapping("/add")
    public ModelAndView add(){
        //rellena el formulario con una rareza vacia, usa el mismo html que editar porque los dos acaban en /save
        ModelAndView mav = new ModelAndView("formularioRareza");

        RarezaDTO rareza = new RarezaDTO();

        mav.addObject("rareza", rareza); //mandamos el objeto vacio para que el form lo rellene

        return mav;
    }

    @GetMapping("/delete/{idRareza}")
    public ModelAndView delete(@PathVariable("idRareza") int idRareza){

        RarezaDTO rareza = new RarezaDTO();
        rareza.setId(idRareza); //solo necesitamos meterle el id para que el repo sepa cual borrar

        rarezaService.delete(rareza); //llamamos al servicio para borrar

        return new ModelAndView("redirect:/rareza"); //recargamos volviendo a la lista de rarezas
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("rareza") RarezaDTO rareza){ //modelattribute recoge el objeto rareza ya relleno del form

        //aqui no hace falta buscar nada extra como en droga, guardamos directo
        rarezaService.save(rareza);

        return new ModelAndView("redirect:/rareza"); //volvemos a la tabla de rarezas
    }

    @GetMapping("/{idRareza}")
    public ModelAndView ver(@PathVariable("idRareza") int idRareza){
        //este tambien es el que se abre cuando le das a "Ver Rareza" desde la tabla de drogas
        ModelAndView mav = new ModelAndView("verRareza");

        RarezaDTO rareza = new RarezaDTO();
        rareza.setId(idRareza);

        rareza = rarezaService.findById(rareza); //machacamos el dto vacio con el que trae todos los datos

        mav.addObject("rareza", rareza); //lo metemos en la bandeja

        return mav;
    }

}

/*
CHULETA RAPIDA:
- En Droga el ID era Long, pero en Rareza pusimos int -> por eso los @PathVariable llevan int idRareza.
- redirect:/rareza -> no abre un html directo, le dice al navegador que vuelva a entrar a la ruta /rareza (el findAll) para que se vea la tabla actualizada.
*/