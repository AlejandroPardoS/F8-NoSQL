package uniandes.edu.co.proyecto.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uniandes.edu.co.proyecto.modelo.Empleado;
import uniandes.edu.co.proyecto.modelo.Usuario;
import uniandes.edu.co.proyecto.repositorio.UsuarioRepository;
import uniandes.edu.co.proyecto.repositorio.CuentaRepository;
import uniandes.edu.co.proyecto.repositorio.EmpleadoRepository;

@Controller
public class InicioSesionController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    CuentaController cuentaController;

    @GetMapping("/iniciar")
    public String iniciarSesion(@RequestParam("numero_documento") String numero_documento, @RequestParam("tipo_documento") String tipo_documento,@RequestParam("password") String clave, Model model) {
        Usuario usuario = usuarioRepository.buscarPorNumDoc(numero_documento);
       
        if (usuario != null) {
            if (clave.equals(usuario.getPalabraClave())){
                String rol = usuario.getRol();
                switch (rol) {
                    case "Empleado":
                        cuentaController.setIdClienteNull();
                        Empleado empleado = empleadoRepository.darEmpleadobyIdUsuario(usuario.getId());
                        empleado.setUsuario(usuario);
                        String rolEmpleado = empleado.getRolEmpleado();

                        if (rolEmpleado.equals("Gerente oficina")){
                            model.addAttribute("gerenteOficina", empleado);
                            //model.addAttribute("esGerenteOficina", true); // Agrega esta línea
                            return "gerenteOficinaInterfaz";
                        } else if (rolEmpleado.equals("Cajero")){
                            model.addAttribute("cajero", empleado);
                            return "cajeroInterfaz";
                        } else {//(rolEmpleado.equals("Gerente general")){
                            model.addAttribute("gerenteGeneral", empleado);
                            return "gerenteGeneralInterfaz";
                        }

                    case "Cliente":
                        model.addAttribute("cliente", usuario);
                        return "clienteInterfaz";

                    case "Administrador":
                        cuentaController.setIdClienteNull();
                        return "administrador";
                    default:
                        return "error"; 
                }
            } else {
                model.addAttribute("errorMessage", "Contraseña incorrecta");
                return "index"; 
            }   
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "index";
        } 
    }
}
