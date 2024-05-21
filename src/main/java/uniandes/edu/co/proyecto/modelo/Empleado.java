package uniandes.edu.co.proyecto.modelo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Data
@Document(collection="empleados")
@ToString
public class Empleado {
    
    @Id
    private ObjectId id;

    //@DBRef
    private ObjectId id_usuario;

    @Field("rolEmpleado")
    private String rolEmpleado;

    //@DBRef
    private ObjectId id_oficina;

    //@DBRef
    private ObjectId id_punto_de_atencion;

    private Usuario usuario;
    private Oficina oficina;
    private PuntoDeAtencion puntoDeAtencion;

    public Empleado(){;}

    public Empleado(ObjectId id, ObjectId id_usuario, String rolEmpleado, ObjectId id_oficina,
    ObjectId id_punto_de_atencion) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.rolEmpleado = rolEmpleado;
        this.id_oficina = id_oficina;
        this.id_punto_de_atencion = id_punto_de_atencion;
    }


}