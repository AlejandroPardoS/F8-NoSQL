package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import uniandes.edu.co.proyecto.modelo.Oficina;
import uniandes.edu.co.proyecto.repositorio.OficinaRepository;

@Controller
public class OficinaController {
    @Autowired
    private OficinaRepository oficinaRepository;

    @GetMapping("/oficina")
    public String oficinas(Model model){
        model.addAttribute("oficina", oficinaRepository.findAll());
        return "oficina";
    }

    @GetMapping("/oficina/new")
    public String oficinaForm(Model model) {
        model.addAttribute("oficina", new Oficina());
        return "oficinaNueva";
    }

    @PostMapping("/oficina/new/save")
    public String oficinaGuardar(@ModelAttribute Oficina oficina) {
        oficinaRepository.save(oficina);
        return "redirect:/oficina";
    }
    
}
