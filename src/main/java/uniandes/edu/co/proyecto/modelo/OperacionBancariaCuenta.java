package uniandes.edu.co.proyecto.modelo;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OperacionBancariaCuenta {

    @Field("fecha")
    private Date fecha;
    @Field("hora")
    private String hora;
    @Field("tipoOperacion")
    private String tipoOperacion;
    @Field("valor")
    private Double valor;
    @Field("numeroCuentaDestino")
    private String numeroCuentaDestino;
    @Field("saldoResultante")
    private Double saldoResultante;
    
    public OperacionBancariaCuenta(Date fecha, String hora, String tipoOperacion, Double valor, String numeroCuentaDestino, Double saldoResultante) {
        this.fecha = fecha;
        this.hora = hora;
        this.tipoOperacion = tipoOperacion;
        this.valor = valor;
        this.numeroCuentaDestino = numeroCuentaDestino;
        this.saldoResultante = saldoResultante;
    }

    

}
