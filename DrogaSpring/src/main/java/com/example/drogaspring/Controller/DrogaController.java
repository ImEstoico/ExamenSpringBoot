package com.example.drogaspring.Controller;

import com.example.drogaspring.models.dto.DrogaDTO;
import com.example.drogaspring.services.DrogaService;
import com.example.drogaspring.services.RarezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/drogas")
public class DrogaController {

    @Autowired
    private DrogaService drogaService;

    @Autowired
    private RarezaService rarezaService;

    @GetMapping("")
    public ModelAndView findAll(){

        ModelAndView mav = new ModelAndView("drogas"); //creamos la bandeja y decimos que abra el archivo drogas.html

        List<DrogaDTO> listadrogas = drogaService.findAll(); //le pedimos al servicio la lista de todas las drogas en formato DTO

        //metemos la lista en la bandeja en la etiqueta de listadrogas
        //va entre comillas es el que se usa en el html -> th:each="droga : ${listadrogas}"
        mav.addObject("listadrogas", listadrogas);

        // devolvemos la bandeja del html con los datos al navegador
        return mav;
    }

    @GetMapping("/update/{idDroga}")
    public ModelAndView editar(@PathVariable("idDroga") long idDroga){ // el pathvariable es para que coja el ${idDroga} de la url

        ModelAndView mav = new ModelAndView("formulario"); //creamos la bandeja y decimos que abra el archivo formulario.html

        DrogaDTO id = new DrogaDTO();
        id.setId(idDroga); //metemos el id en un dto vacio para poder buscarlo

        DrogaDTO droga = drogaService.findById(id); //buscamos en el servicio

        mav.addObject("droga", droga); //mandamos al html (NOMBRE DEL TAL), (OBJETO/clase)
        mav.addObject("listararezas", rarezaService.findAll()); //mandamos tambien las rarezas para el desplegable

        return mav;
    }

    @GetMapping("/add")
    public ModelAndView add(){
        //rellena el formulario con droga vacia, es como el editar porque es save;
        // se usa el mismo formulario para crear y editar

        ModelAndView mav = new ModelAndView("formulario");

        DrogaDTO droga = new DrogaDTO();

        mav.addObject("droga", droga);
        mav.addObject("listararezas", rarezaService.findAll()); //mandamos la lista de rarezas para poder elegir una

        return mav;
    }

    @GetMapping("/delete/{droga}")
    public ModelAndView delete(@PathVariable("droga") long IdDroga){

        DrogaDTO droga = new DrogaDTO();
        droga.setId(IdDroga); //le ponemos solo el id porque el delete busca por id

        drogaService.delete(droga); //le decimos al servicio que la borre

        return new ModelAndView("redirect:/drogas"); //recargamos volviendo a la tabla principal
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("droga") DrogaDTO droga){ //modelattribute pilla todo el objeto relleno del formulario

        //el formulario solo nos da el id de la rareza, asi que buscamos la rareza entera y se la metemos a la droga
        droga.setRarezaDTO(rarezaService.findById(droga.getRarezaDTO()));

        drogaService.save(droga); //guardamos la droga en el servicio

        return new ModelAndView("redirect:/drogas"); //volvemos a la lista
    }

    @GetMapping("/{droga}")
    public ModelAndView ver(@PathVariable("droga") long IdDroga){

        ModelAndView mav = new ModelAndView("ver"); //abrimos ver.html

        DrogaDTO droga = new DrogaDTO();
        droga.setId(IdDroga);

        droga = drogaService.findById(droga); //sustituimos la droga vacia por la encontrada con todos sus datos

        mav.addObject("droga", droga); //la metemos en la bandeja para el html

        return mav;
    }

}

/*
CHULETA RAPIDA:
PathVariable -> int/long/string (un dato suelto de la url) -> GET
(Puede ir en cualquier otro porque es parte de una url)

ModelAttribute -> object (el objeto entero que viene del form) -> POST
*/