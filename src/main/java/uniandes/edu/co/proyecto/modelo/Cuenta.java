package uniandes.edu.co.proyecto.modelo;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Data
@Document(collection="cuentas")
@ToString
public class Cuenta {
    
    @Id
    private ObjectId id;

    @Field("numeroCuenta")
    private String numeroCuenta;

    @DBRef
    private ObjectId id_usuario;

    @Field("tipoCuenta")
    private String tipoCuenta;
    @Field("estadoCuenta")
    private String estadoCuenta;
    @Field("saldo")
    private Double saldo;
    @Field("fechaCreacion")
    private Date fechaCreacion;

    private List<OperacionBancariaCuenta> operacionesBancarias;

    private Usuario usuario;

    public Cuenta(){;}

    public Cuenta(ObjectId id, String numeroCuenta, ObjectId id_usuario, String tipoCuenta, String estadoCuenta, Double saldo,
            Date fechaCreacion, List<OperacionBancariaCuenta> operacionesBancarias) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.id_usuario = id_usuario;
        this.tipoCuenta = tipoCuenta;
        this.estadoCuenta = estadoCuenta;
        this.saldo = saldo;
        this.fechaCreacion = fechaCreacion;
        this.operacionesBancarias = operacionesBancarias;
    }

}
