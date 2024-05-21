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
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.modelo.Empleado;
import uniandes.edu.co.proyecto.modelo.Oficina;
import uniandes.edu.co.proyecto.modelo.PuntoDeAtencion;
import uniandes.edu.co.proyecto.modelo.Usuario;
import uniandes.edu.co.proyecto.repositorio.EmpleadoRepository;
import uniandes.edu.co.proyecto.repositorio.OficinaRepository;
import uniandes.edu.co.proyecto.repositorio.PuntoDeAtencionRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioRepository;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EmpleadoController {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OficinaRepository oficinaRepository;

    @Autowired
    private PuntoDeAtencionRepository puntoDeAtencionRepository;


    @GetMapping("/empleado")
    public String empleados(Model model){
        List<Empleado> empleados = obtenerEmpleadosAsociadosAOficinaYPtoAtencion();
        model.addAttribute("empleado", empleados);
        return "empleado";
    }


    @GetMapping("/empleado/new")
    public String empleadoForm(Model model) {
        model.addAttribute("empleado", new Empleado());
        List<PuntoDeAtencion> puntosDeAtencion = puntoDeAtencionRepository.findAll();
        List<PuntoDeAtencion> puntosConOficina = new ArrayList<>();
        for (PuntoDeAtencion puntoDeAtencion : puntosDeAtencion) {

            Oficina oficinaPunto;
            if (puntoDeAtencion.getIdOficina() != null) {
                Optional<Oficina> oficina = oficinaRepository.findById(puntoDeAtencion.getIdOficina());
                oficinaPunto = oficina.get();
                puntoDeAtencion.setOficina(oficinaPunto);
                puntosConOficina.add(puntoDeAtencion);
            }        
        }
        model.addAttribute("puntoDeAtencion", puntosConOficina);
        return "empleadoNuevo";
    }
    

    @PostMapping("/empleado/new/save")
    public String empleadoSinOficinaNiPtoAtencion(Model model, @ModelAttribute Usuario usuario, 
            @RequestParam("rolEmpleado") String rolEmpleado, @RequestParam("id_oficina") String id_oficinaStr,
            @RequestParam("id_punto_de_atencion") String id_punto_de_atencionStr) {

        usuarioRepository.save(usuario);

        ObjectId id_oficina = new ObjectId(id_oficinaStr);
        ObjectId id_punto_de_atencion = new ObjectId(id_punto_de_atencionStr);

        Optional<Oficina> oficina = oficinaRepository.findById(id_oficina);
        if (oficina.isEmpty()) {
            return "redirect:/";
        }
        Optional<PuntoDeAtencion> puntoDeAtencion = puntoDeAtencionRepository.findById(id_punto_de_atencion);
        if (puntoDeAtencion.isEmpty()) {
            return "redirect:/";
        }

        Empleado empleado = new Empleado();
        empleado.setId_usuario(usuario.getId());
        empleado.setRolEmpleado(rolEmpleado);
        empleado.setId_oficina(id_oficina);
        empleado.setId_punto_de_atencion(id_punto_de_atencion);

        empleadoRepository.save(empleado);
        List<Empleado> empleados = obtenerEmpleadosAsociadosAOficinaYPtoAtencion();
        model.addAttribute("empleado", empleados);
        return "empleado";
    }


    public List<Empleado> obtenerEmpleadosAsociadosAOficinaYPtoAtencion() {

        List<Empleado> empleados = empleadoRepository.findAll();
        for (Empleado empleado : empleados) {
            Optional<Usuario> usuario = usuarioRepository.findById(empleado.getId_usuario());
            empleado.setUsuario(usuario.get());

            ObjectId id_oficina = empleado.getId_oficina();
            Oficina oficinaEmpleado;
            if (id_oficina != null) {
                Optional<Oficina> oficina = oficinaRepository.findById(empleado.getId_oficina());
                if (!oficina.isEmpty()) {
                    oficinaEmpleado = oficina.get();
                } else {
                    oficinaEmpleado = null;
                }
            }
            else 
                oficinaEmpleado = null;
            empleado.setOficina(oficinaEmpleado);

            ObjectId id_punto_de_atencion = empleado.getId_punto_de_atencion();
            PuntoDeAtencion puntoDeAtencionEmpleado;
            if (id_punto_de_atencion != null) {
                Optional<PuntoDeAtencion> puntoDeAtencion = puntoDeAtencionRepository.findById(empleado.getId_punto_de_atencion());
                if (!puntoDeAtencion.isEmpty()) {
                    puntoDeAtencionEmpleado = puntoDeAtencion.get();
                } else {
                    puntoDeAtencionEmpleado = null;
                }
            }
            else 
                puntoDeAtencionEmpleado = null;
            empleado.setPuntoDeAtencion(puntoDeAtencionEmpleado);
        }
        return empleados;
    }

}
