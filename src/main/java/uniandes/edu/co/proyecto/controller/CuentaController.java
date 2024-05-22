package uniandes.edu.co.proyecto.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        @ModelAttribute("fechaCreacion") String fechaCreacion, @ModelAttribute("hora") String hora, Model model) throws ParseException {

        Usuario usuario = usuarioRepository.buscarPorNumDoc(numeroDocumento);
        if (usuario != null) {
            Cuenta cuenta = new Cuenta();
            cuenta.setId_usuario(usuario.getId());
            //GENERAR UNA SECUENCIA DE NUMERO DE CUENTA
            //CON RANDOM DE 1 A 100000000000
            cuenta.setNumeroCuenta(generarNumeroCuenta());
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
            model.addAttribute("errorMessage", "Usuario no encontrado");
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
        Cuenta cuenta2 = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        ObjectId idCuenta = cuenta2.getId();
        if (cuenta.getEstadoCuenta().equals("Cerrada")){
            if (cuenta.getSaldo() == 0){
                cuentaRepository.actualizarEstadoCuenta(idCuenta, cuenta.getEstadoCuenta());
                cuentaRepository.actualizarTipoCuenta(idCuenta, cuenta.getTipoCuenta());
                return "redirect:/";
            } else
                model.addAttribute("errorMessage", "Cuenta no puede ser cerrada con saldo diferente de 0");
                return "error";
        } else {
            cuentaRepository.actualizarEstadoCuenta(idCuenta, cuenta.getEstadoCuenta());
            cuentaRepository.actualizarTipoCuenta(idCuenta, cuenta.getTipoCuenta());
            
            return "redirect:/";}

    }


    @GetMapping("/cuenta/operaciones/{numeroCuenta}")
    public String operacionesCuenta(@PathVariable("numeroCuenta") String numeroCuenta, Model model) {
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        Usuario usuario = usuarioRepository.buscarPorId(cuenta.getId_usuario());
        List<OperacionBancariaCuenta> operaciones = cuenta.getOperacionesBancarias();
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("usuario", usuario);
        model.addAttribute("operacionBancariaCuenta", operaciones);
        return "operacionBancariaCuenta";
    }

    @GetMapping("/cuenta/operaciones/consignar/{numeroCuenta}")
    public String consignarForm(@PathVariable("numeroCuenta") String numeroCuenta, Model model) {
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        model.addAttribute("cuenta", cuenta);
        return "operacionConsignar";
    }

    @PostMapping("/cuenta/operaciones/consignar/{numeroCuenta}/save")
    public String consignarGuardar(@PathVariable("numeroCuenta") String numeroCuenta, @ModelAttribute("valor") Double valor, 
            @ModelAttribute("fecha") String fecha, @ModelAttribute("hora") String hora, Model model) throws ParseException {
        
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        if (cuenta.getEstadoCuenta().equals("Activa")) {
            cuenta.setSaldo(cuenta.getSaldo() + valor);
            cuentaRepository.actualizarSaldo(cuenta.getId(), cuenta.getSaldo());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date fechaDate = dateFormat.parse(fecha);
            OperacionBancariaCuenta opConsignacion = new OperacionBancariaCuenta(fechaDate, hora, "Consignar", valor, null, cuenta.getSaldo());
            cuenta.getOperacionesBancarias().add(opConsignacion);
            cuentaRepository.save(cuenta);
            return "redirect:/cuenta/operaciones/" + numeroCuenta;
        } else
            model.addAttribute("errorMessage", "Cuenta desactivada no se puede consignar");
            return "error";
    }

    @GetMapping("/cuenta/operaciones/retirar/{numeroCuenta}")
    public String retirarForm(@PathVariable("numeroCuenta") String numeroCuenta, Model model) {
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        model.addAttribute("cuenta", cuenta);
        return "operacionRetirar";
    }

    @PostMapping("/cuenta/operaciones/retirar/{numeroCuenta}/save")
    public String retirarGuardar(@PathVariable("numeroCuenta") String numeroCuenta, @ModelAttribute("valor") Double valor, 
            @ModelAttribute("fecha") String fecha, @ModelAttribute("hora") String hora, Model model) throws ParseException {
        
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        if (cuenta.getEstadoCuenta().equals("Activa")) {
            if (cuenta.getSaldo() >= valor) {
                cuenta.setSaldo(cuenta.getSaldo() - valor);
                cuentaRepository.actualizarSaldo(cuenta.getId(), cuenta.getSaldo());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date fechaDate = dateFormat.parse(fecha);
                OperacionBancariaCuenta opConsignacion = new OperacionBancariaCuenta(fechaDate, hora, "Retirar", valor, null, cuenta.getSaldo());
                cuenta.getOperacionesBancarias().add(opConsignacion);
                cuentaRepository.save(cuenta);
                return "redirect:/cuenta/operaciones/" + numeroCuenta;
            } else
                model.addAttribute("errorMessage", "Saldo insuficiente para realizar la transacción");
                return "error";
        } else
            model.addAttribute("errorMessage", "Cuenta desactivada no se puede consignar");
            return "error";
    }

    @GetMapping("/cuenta/operaciones/transferir/{numeroCuenta}")
    public String transferirForm(@PathVariable("numeroCuenta") String numeroCuenta, Model model) {
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        model.addAttribute("cuenta", cuenta);
        return "operacionTransferir";
    }

    @PostMapping("/cuenta/operaciones/transferir/{numeroCuenta}/save")
    public String transferirGuardar(@PathVariable("numeroCuenta") String numeroCuenta, @ModelAttribute("valor") Double valor, 
            @ModelAttribute("numeroCuentaDestino") String numeroCuentaDestino, @ModelAttribute("fecha") String fecha, 
            @ModelAttribute("hora") String hora, Model model) throws ParseException {
        
        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        Cuenta cuentaDestino = cuentaRepository.buscarPorNumeroCuenta(numeroCuentaDestino);
        if (cuenta.getEstadoCuenta().equals("Activa") && cuentaDestino.getEstadoCuenta().equals("Activa")) {
            if (cuenta.getSaldo() >= valor) {
                cuenta.setSaldo(cuenta.getSaldo() - valor);
                cuentaDestino.setSaldo(cuentaDestino.getSaldo() + valor);
                cuentaRepository.actualizarSaldo(cuenta.getId(), cuenta.getSaldo());
                cuentaRepository.actualizarSaldo(cuentaDestino.getId(), cuentaDestino.getSaldo());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date fechaDate = dateFormat.parse(fecha);
                OperacionBancariaCuenta opConsignacion = new OperacionBancariaCuenta(fechaDate, hora, "Transferir", valor, cuentaDestino.getNumeroCuenta(), cuenta.getSaldo());
                cuenta.getOperacionesBancarias().add(opConsignacion);
                cuentaRepository.save(cuenta);
                /*OperacionBancariaCuenta opConsignacionDestino = new OperacionBancariaCuenta(fechaDate, hora, "Transferencia recibida", valor, cuenta.getNumeroCuenta(), cuentaDestino.getSaldo());
                cuentaDestino.getOperacionesBancarias().add(opConsignacionDestino);
                cuentaRepository.save(cuentaDestino);*/
                return "redirect:/cuenta/operaciones/" + numeroCuenta;
            } else
                model.addAttribute("errorMessage", "Saldo insuficiente para realizar la transacción");
                return "error";
        } else
            model.addAttribute("errorMessage", "Cuenta desactivada no se puede consignar");
            return "error";
    }


    public String generarNumeroCuenta() {
        Random rand = new Random();
        int digitos = 10;
        long limiteInferior = (long) Math.pow(10, digitos - 1);
        long limiteSuperior = (long) Math.pow(10, digitos) - 1;
        long numeroCuenta = limiteInferior + ((long) (rand.nextDouble() * (limiteSuperior - limiteInferior)));
        return String.valueOf(numeroCuenta);
}
    
}
