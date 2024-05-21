package uniandes.edu.co.proyecto.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<PuntoDeAtencion> puntosDeAtencion = puntoDeAtencionRepository.findAll();
        for (PuntoDeAtencion puntoDeAtencion : puntosDeAtencion) {

            ObjectId id_oficina = puntoDeAtencion.getIdOficina();
            Oficina oficinaPto;
            if (id_oficina != null) {
                Optional<Oficina> oficina = oficinaRepository.findById(puntoDeAtencion.getIdOficina());
                if (!oficina.isEmpty()) {
                    oficinaPto = oficina.get();
                } else {
                    oficinaPto = null;
                }
            } else 
                oficinaPto = null;
            puntoDeAtencion.setOficina(oficinaPto);
        }
        model.addAttribute("puntoDeAtencion", puntosDeAtencion);
        return "puntoDeAtencion";
    }

    @GetMapping("/puntoDeAtencion/new")
    public String puntoDeAtencionForm(Model model) {
        model.addAttribute("puntoDeAtencion", new PuntoDeAtencion());
        List<Oficina> oficinas = oficinaRepository.findAll();
        model.addAttribute("oficina", oficinas);
        return "puntoDeAtencionNuevo";
    }

    @PostMapping("/puntoDeAtencion/new/save")
    public String puntoDeAtencionGuardar(@RequestParam("latitud") String latitud, @RequestParam("longitud") String longitud,
        @RequestParam("tipo") String tipo, @RequestParam("idOficina") String idOficina) {

        ObjectId idOficinaObj = new ObjectId(idOficina);

        PuntoDeAtencion puntoDeAtencion = new PuntoDeAtencion();
        puntoDeAtencion.setLatitud(Double.parseDouble(latitud));
        puntoDeAtencion.setLongitud(Double.parseDouble(longitud));
        puntoDeAtencion.setTipo(tipo);
        puntoDeAtencion.setIdOficina(idOficinaObj);
        puntoDeAtencionRepository.save(puntoDeAtencion);

        oficinaRepository.incrementarPuntosDeAtencion(idOficinaObj);
        Optional<Oficina> oficina = oficinaRepository.findById(idOficinaObj);
        puntoDeAtencion.setOficina(oficina.get());
        return "redirect:/puntoDeAtencion";
    }


    @GetMapping("/puntoDeAtencion/{id}/delete")
    public String puntoDeAtencionEliminar(@PathVariable("id") ObjectId id) {
        oficinaRepository.disminuirPuntosDeAtencion(puntoDeAtencionRepository.findById(id).get().getIdOficina());
        puntoDeAtencionRepository.deleteById(id);
        return "redirect:/puntoDeAtencion";
    }
}
