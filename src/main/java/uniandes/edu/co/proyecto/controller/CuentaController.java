package uniandes.edu.co.proyecto.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import org.bson.types.ObjectId;
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
    public String cuentaEditarGuardar(@PathVariable("numeroCuenta") String numeroCuenta, @ModelAttribute Cuenta cuenta, Model model) throws ParseException {
        Cuenta cuenta2 = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        ObjectId idCuenta = cuenta2.getId();
        if (cuenta.getEstadoCuenta().equals("Cerrada")){
            if (cuenta.getSaldo() == 0){
                cuentaRepository.actualizarEstadoCuenta(idCuenta, cuenta.getEstadoCuenta()); //RARO
                cuentaRepository.actualizarTipoCuenta(idCuenta, cuenta.getTipoCuenta());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date now = new Date();
                
                // Formatear la fecha y la hora actuales
                String fechaActualStr = dateFormat.format(now);
                
                // Obtener solo la hora actual en formato "HH:mm:ss"
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String horaActualStr = timeFormat.format(now);
                OperacionBancariaCuenta opCierre = new OperacionBancariaCuenta(dateFormat.parse(fechaActualStr), horaActualStr, "Cierre", 0.0, null, 0.0);
                cuenta2.getOperacionesBancarias().add(opCierre);
                cuentaRepository.save(cuenta2);
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
                OperacionBancariaCuenta opTransferencia = new OperacionBancariaCuenta(fechaDate, hora, "Transferir", valor, cuentaDestino.getNumeroCuenta(), cuenta.getSaldo());
                cuenta.getOperacionesBancarias().add(opTransferencia);
                cuentaRepository.save(cuenta);

                OperacionBancariaCuenta opConsignacionPorTransferencia = new OperacionBancariaCuenta(fechaDate, hora, "Consignar", valor, null, cuentaDestino.getSaldo());
                cuentaDestino.getOperacionesBancarias().add(opConsignacionPorTransferencia);
                cuentaRepository.save(cuentaDestino);
                return "redirect:/cuenta/operaciones/" + numeroCuenta;
            } else
                model.addAttribute("errorMessage", "Saldo insuficiente para realizar la transacción");
                return "error";
        } else
            model.addAttribute("errorMessage", "Cuenta desactivada no se puede consignar");
            return "error";
    }

    @GetMapping("/cuenta/filtrarCuentasPorTipo")
    public String filtroCuentasPorTipo(@RequestParam("tipoCuenta") String tipoCuenta, Model model) {
        if (tipoCuenta.equals("Todos")){
            List<Cuenta> cuentas = cuentaRepository.findAll();
            for (Cuenta cuenta : cuentas) {
                Usuario usuario = usuarioRepository.buscarPorId(cuenta.getId_usuario());
                cuenta.setUsuario(usuario);
            }
            model.addAttribute("cuenta", cuentas);
            return "cuenta";
        } else {
            List<Cuenta> cuentas = cuentaRepository.buscarPorTipoCuenta(tipoCuenta);
            for (Cuenta cuenta : cuentas) {
                Usuario usuario = usuarioRepository.buscarPorId(cuenta.getId_usuario());
                cuenta.setUsuario(usuario);
            }
            model.addAttribute("cuenta", cuentas);
            return "cuenta";
        }
    }

    @GetMapping("/cuenta/filtrarCuentasPorSaldo")
    public String filtroCuentasPorSaldo(@RequestParam("minSaldo") Double minSaldo, 
                @RequestParam("maxSaldo") Double maxSaldo, Model model) {

        List<Cuenta> cuentasFiltradas = cuentaRepository.buscarPorRangoSaldo(minSaldo, maxSaldo);
        model.addAttribute("cuenta", cuentasFiltradas);
        return "cuenta";
    
    }

    @GetMapping("/cuenta/filtrarCuentasPorNumDocCliente")
    public String filtroCuentasPorCliente(@RequestParam("numeroDocumento") String numeroDocumento, Model model) {
        Usuario usuario = usuarioRepository.buscarPorNumDoc(numeroDocumento);
        List<Cuenta> cuentasFiltradas = cuentaRepository.buscarPorIdUsuario(usuario.getId());
        for (Cuenta cuenta : cuentasFiltradas) {
            cuenta.setUsuario(usuario);
        }
        model.addAttribute("cuenta", cuentasFiltradas);
        return "cuenta";
    }

    @GetMapping("/cuenta/filtrarCuentasPorFechaCreacion")
    public String filtroCuentasPorFechaCreacion(@RequestParam("fechaInicio") String fechaInicio, 
                        @RequestParam("fechaFin") String fechaFin, Model model) throws ParseException {
        
        // Paso 1: Crear SimpleDateFormat para el formato original
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Paso 2: Parsear la fecha original
        String originalDateInicio = fechaInicio;
        String originalDateFin = fechaFin;
        Date dateInicio = originalFormat.parse(originalDateInicio);
        Date dateFin = originalFormat.parse(originalDateFin);

        // Paso 3: Usar Calendar para establecer los campos de tiempo a 59
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(dateFin);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        Date dateFinFormatted = calendar.getTime();

        // Paso 4: Crear SimpleDateFormat para el formato deseado
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        desiredFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Paso 5: Formatear la fecha al nuevo formato
        String formattedDateInicioStr = desiredFormat.format(dateInicio);
        String formattedDateFinStr = desiredFormat.format(dateFinFormatted);

        // Paso 6: Parsear la cadena formateada de vuelta a Date
        Date formattedDateInicio = desiredFormat.parse(formattedDateInicioStr);
        Date formattedDateFin = desiredFormat.parse(formattedDateFinStr);
                        
        List<Cuenta> cuentasFiltradas = cuentaRepository.buscarPorRangoFecha(formattedDateInicio, formattedDateFin);
        for (Cuenta cuenta : cuentasFiltradas) {
            Usuario usuario = usuarioRepository.buscarPorId(cuenta.getId_usuario());
            cuenta.setUsuario(usuario);
        }
        model.addAttribute("cuenta", cuentasFiltradas);
        return "cuenta";
    }

    @GetMapping("/operacionBancariaCuenta/extractoBancario/sol")
    public String extractoBancarioHacer(Model model){
        return "extractoBancario";
    }


    @GetMapping("/operacionBancariaCuenta/extractoBancario")
    public String extractoBancario(@RequestParam("numeroCuenta") String numeroCuenta, @RequestParam("mes_anio") String mes_anio, Model model) throws ParseException {

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta);
        String[] parts = mes_anio.split("-");
        String mesStr = parts[1];
        String anioStr = parts[0];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date inicioMes = null;
        Date finMes = null;

        String fechaInicioStr = anioStr + "-" + mesStr + "-01T00:00:00.000+00:00";
        inicioMes = dateFormat.parse(fechaInicioStr);

        // Crear un calendario para manipular la fecha
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inicioMes);

        // Establecer el último día del mes
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String fechaFinStr = anioStr + "-" + mesStr + "-" + lastDay + "T23:59:59.999+00:00";
        finMes = dateFormat.parse(fechaFinStr);

        if (cuenta != null) {
            Cuenta cuentaOps = cuentaRepository.extractoMensual(numeroCuenta, inicioMes, finMes);
            Double saldoInicial = 0.0;
            Double saldoFinal = 0.0;
            int i = 0;
            for (OperacionBancariaCuenta op : cuentaOps.getOperacionesBancarias()) {
                if (i==0)
                    saldoInicial = op.getSaldoResultante() - op.getValor();
                if (i==cuentaOps.getOperacionesBancarias().size()-1) //ojo aca
                    saldoFinal = op.getSaldoResultante();
                i++;
            }
            model.addAttribute("saldoInicial", saldoInicial);
            model.addAttribute("saldoFinal", saldoFinal);
            model.addAttribute("cuenta", cuentaOps);
            model.addAttribute("operacionBancariaCuenta", cuentaOps.getOperacionesBancarias());
            model.addAttribute("mes_anio", mes_anio);
            return "extractoVer";
        } else {
            return "redirect:/clienteInterfaz";
        }
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
