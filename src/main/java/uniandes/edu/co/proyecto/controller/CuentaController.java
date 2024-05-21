package uniandes.edu.co.proyecto.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uniandes.edu.co.proyecto.modelo.Cuenta;
import uniandes.edu.co.proyecto.modelo.OperacionBancariaCuenta;
import uniandes.edu.co.proyecto.modelo.Usuario;
import uniandes.edu.co.proyecto.repositorio.CuentaRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioRepository;

@Controller
public class CuentaController {

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/cuenta")
    public String cuentas(Model model) {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        for (Cuenta cuenta : cuentas) {
            Usuario usuario = usuarioRepository.buscarPorId(cuenta.getId_usuario());
            cuenta.setUsuario(usuario);
        }
        model.addAttribute("cuenta", cuentas);
        return "cuenta";
    }

    /*@GetMapping("/cuenta/oficina/{idOficina}")
    public String cuentasPorOficina(@PathVariable("idOficina") Integer idOficina, @RequestParam(value = "gerenteOficina", required = false) Integer gerenteOficina, Model model) {
    model.addAttribute("cuenta", cuentaRepository.darCuentasPorOficina(idOficina));
    this.idOficina = idOficina;
    if (gerenteOficina != null) {
        model.addAttribute("esGerenteOficina", true);
    }
    return "cuenta";
}

    @GetMapping("/cuenta/{id_cliente}")
    public String cuentasPorCliente(@ModelAttribute("id_cliente") Integer id_cliente, Model model) {
        Collection<Cuenta> cuenta = cuentaRepository.darCuentaPorId_cliente(id_cliente);
        if (!cuenta.isEmpty()) {
                model.addAttribute("cuenta", cuenta);
                this.idCliente = id_cliente;
            return "cuenta";
        } else {
            return "redirect:/";
        }
    }*/

    @GetMapping("/cuenta/new")
    public String cuentaForm(Model model) {
        model.addAttribute("cuenta", new Cuenta());
        return "cuentaNuevo";
    }

    @PostMapping("/cuenta/new/save")
    public String cuentaGuardar(@ModelAttribute("numeroDocumento") String numeroDocumento, 
        @ModelAttribute("tipoDocumento") String tipoDocumento, @ModelAttribute("tipoCuenta") String tipoCuenta,
        @ModelAttribute("estadoCuenta") String estadoCuenta, @ModelAttribute("saldo") Double saldo, 
        @ModelAttribute("fechaCreacion") String fechaCreacion, @ModelAttribute("hora") String hora) throws ParseException {

        Usuario usuario = usuarioRepository.buscarPorNumDoc(numeroDocumento);
        if (usuario != null) {
            Cuenta cuenta = new Cuenta();
            cuenta.setId_usuario(usuario.getId());
            cuenta.setNumeroCuenta("1234567990"); //GENERAR UNA SECUENCIA DE NUMERO DE CUENTA QUE NO SE REPITA
            //CON RANDOM DE 1 A 100000000000
            
            cuenta.setTipoCuenta(tipoCuenta);
            cuenta.setEstadoCuenta(estadoCuenta);
            cuenta.setSaldo(saldo);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date fechaDate = dateFormat.parse(fechaCreacion);
            cuenta.setFechaCreacion(fechaDate);
            OperacionBancariaCuenta opCreacion = new OperacionBancariaCuenta(cuenta.getFechaCreacion(), hora, "Apertura", cuenta.getSaldo(), null, cuenta.getSaldo());
            cuenta.setOperacionesBancarias(List.of(opCreacion));
            cuentaRepository.save(cuenta);
        } else {
            return "error";
        }
        
        return "redirect:/cuenta";
    }
    
    @GetMapping("/cuenta/{numeroCuenta}/edit")
    public String cuentaEditarForm(@PathVariable("numeroCuenta") String numeroCuenta, Model model) {
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        if (cuenta != null) {
            model.addAttribute("cuenta", cuenta);
            return "cuentaEditar";
        } else {
            return "redirect:/cuenta";
        }
    }

    @PostMapping("/cuenta/{numeroCuenta}/edit/save")
    public String cuentaEditarGuardar(@PathVariable("numeroCuenta") String numeroCuenta, @ModelAttribute Cuenta cuenta, Model model) {

        //Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        if (cuenta.getEstadoCuenta().equals("Cerrada")){
            if (cuenta.getSaldo() == 0){
                cuentaRepository.actualizarEstadoCuenta(cuenta.getId(), cuenta.getEstadoCuenta());
                cuentaRepository.actualizarTipoCuenta(cuenta.getId(), cuenta.getTipoCuenta());
                return "redirect:/";
            } else
                model.addAttribute("errorMessage", "Cuenta no puede ser cerrada con saldo diferente de 0");
                return "error";
        } else {
            cuentaRepository.actualizarEstadoCuenta(cuenta.getId(), cuenta.getEstadoCuenta()); //NO ESTÁ FUNCIONANDO
            cuentaRepository.actualizarTipoCuenta(cuenta.getId(), cuenta.getTipoCuenta()); //NO ESTÁ FUNCIONANDO
            
            return "redirect:/";}

    }
    /* 
    @GetMapping("/cuenta/filtrarCuentasPorTipo")
    public String filtroCuentasPorTipo(@RequestParam("tipoCuenta") String tipoCuenta, Model model) {
        if (idCliente != null) {
            if (tipoCuenta.equals("Todos")){
                Collection<Cuenta> cuentas = cuentaRepository.darCuentaPorId_cliente(idCliente);
                model.addAttribute("cuenta", cuentas);
                return "cuenta";
            } else {
                Collection<Cuenta> cuentas = cuentaRepository.darCuentaPorTipoCuentaYCliente(tipoCuenta, idCliente);
                model.addAttribute("cuenta", cuentas);
                return "cuenta";
            }
        }
        else if (idOficina != null) {
            if (tipoCuenta.equals("Todos")){
                Collection<Cuenta> cuentas = cuentaRepository.darCuentasPorOficina(idOficina);
                model.addAttribute("cuenta", cuentas);
                return "cuenta";
            } else {
                Collection<Cuenta> cuentas = cuentaRepository.darCuentaPorTipoCuentaYOficina(tipoCuenta, idOficina);
                model.addAttribute("cuenta", cuentas);
                return "cuenta";
            }
        }
        else if (idCliente == null && idOficina == null) {
            if (tipoCuenta.equals("Todos")){
                Collection<Cuenta> cuentas = cuentaRepository.darCuentas();
                model.addAttribute("cuenta", cuentas);
                return "cuenta";
            } else {
                Collection<Cuenta> cuentas = cuentaRepository.darCuentaPorTipoCuenta(tipoCuenta);
                model.addAttribute("cuenta", cuentas);
                return "cuenta";
            }
        }
        else {
            return "redirect:/";
        }
    }

    @GetMapping("/cuenta/filtrarCuentasPorSaldo")
    public String filtroCuentasPorSaldo(@RequestParam("minSaldo") Double minSaldo, 
                                     @RequestParam("maxSaldo") Double maxSaldo, 
                                     Model model) {
    if (idCliente != null) {
        Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorRangoSaldoYCliente(minSaldo, maxSaldo, idCliente);
        model.addAttribute("cuenta", cuentasFiltradas);
        return "cuenta";
    }
    else if (idOficina != null) {
        Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorRangoSaldoYOficina(minSaldo, maxSaldo, idOficina);
        model.addAttribute("cuenta", cuentasFiltradas);
        return "cuenta";
    }
    else if (idCliente == null && idOficina == null) {
        Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorRangoSaldo(minSaldo, maxSaldo);
        model.addAttribute("cuenta", cuentasFiltradas);
        return "cuenta";
    }
    else {
        return "redirect:/";
    }
}

    @GetMapping("/cuenta/filtrarCuentasPorFechaCreacion")
    public String filtroCuentasPorFechaCreacion(@RequestParam("fechaInicio") Date fechaInicio, 
                                                @RequestParam("fechaFin") Date fechaFin, 
                                                Model model) {
        if (idCliente != null){
            Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorFechaCreacionYCliente(fechaInicio, fechaFin, idCliente);
            model.addAttribute("cuenta", cuentasFiltradas);
            return "cuenta";
        }
        else if (idOficina != null){
            Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorFechaCreacionYOficina(fechaInicio, fechaFin, idOficina);
            model.addAttribute("cuenta", cuentasFiltradas);
            return "cuenta";
        }
        else if (idCliente == null && idOficina == null) {
            Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorFechaCreacion(fechaInicio, fechaFin);
            model.addAttribute("cuenta", cuentasFiltradas);
            return "cuenta";
        }
        else {
            return "redirect:/";
        }
    }

    @GetMapping("/cuenta/filtrarCuentasPorFechaUltTrans")
    public String filtroCuentasPorFechaUltTrans(@RequestParam("fechaInicio") Date fechaInicio, 
                                                @RequestParam("fechaFin") Date fechaFin, 
                                                Model model) {
        if(idCliente != null){
            Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorFechaUltTransYCliente(fechaInicio, fechaFin, idCliente);
            model.addAttribute("cuenta", cuentasFiltradas);
            return "cuenta";
        }
        else if (idOficina != null){
            Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorFechaUltTransYOficina(fechaInicio, fechaFin, idOficina);
            model.addAttribute("cuenta", cuentasFiltradas);
            return "cuenta";
        }
        else if (idCliente == null && idOficina == null) {
            Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorFechaUltTrans(fechaInicio, fechaFin);
            model.addAttribute("cuenta", cuentasFiltradas);
            return "cuenta";
        }
        else {
            return "redirect:/";
        }
    }

    @GetMapping("/cuenta/filtrarCuentasPorNumDocCliente")
    public String filtroCuentasPorFechaUltTrans(@RequestParam("numeroDocumento") String numeroDocumento, 
                                                Model model) {
        Collection<Cuenta> cuentasFiltradas = cuentaRepository.darCuentasPorNumeroDocumentCliente(numeroDocumento.trim());
        model.addAttribute("cuenta", cuentasFiltradas);
        return "cuenta";
    }

    @GetMapping("/cuenta/{numeroCuenta}/delete")
    public String cuentaEliminar(@PathVariable("numeroCuenta") String numeroCuenta) {
        cuentaRepository.eliminarCuenta(numeroCuenta);
        return "redirect:/cuenta";
    }*/
    
}
