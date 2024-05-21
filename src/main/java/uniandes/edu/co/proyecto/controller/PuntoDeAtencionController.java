package uniandes.edu.co.proyecto.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.modelo.Oficina;
import uniandes.edu.co.proyecto.modelo.PuntoDeAtencion;
import uniandes.edu.co.proyecto.repositorio.OficinaRepository;
import uniandes.edu.co.proyecto.repositorio.PuntoDeAtencionRepository;

@Controller
public class PuntoDeAtencionController {

    @Autowired
    private PuntoDeAtencionRepository puntoDeAtencionRepository;

    @Autowired
    private OficinaRepository oficinaRepository;

    @GetMapping("/puntoDeAtencion")
    public String puntosDeAtencion(Model model){
        model.addAttribute("puntoDeAtencion", puntoDeAtencionRepository.findAll());
        return "puntoDeAtencion";
    }

    @GetMapping("/puntoDeAtencion/new")
    public String puntoDeAtencionForm(Model model) {
        model.addAttribute("puntoDeAtencion", new PuntoDeAtencion());
        return "puntoDeAtencionNuevo";
    }

    @PostMapping("/puntoDeAtencion/new/save")
    public String puntoDeAtencionGuardar(@ModelAttribute PuntoDeAtencion puntoDeAtencion) {
        puntoDeAtencionRepository.save(puntoDeAtencion);
        return "redirect:/puntoDeAtencion";
    }


    @GetMapping("/puntoDeAtencion/{id}/delete")
    public String puntoDeAtencionEliminar(@PathVariable("id") ObjectId id) {
        puntoDeAtencionRepository.deleteById(id);
        return "redirect:/puntoDeAtencion";
    }
}
