package uniandes.edu.co.proyecto.modelo;

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
    private String id;

    @DBRef
    private Usuario id_usuario;

    @Field("rolEmpleado")
    private String rolEmpleado;

    @DBRef
    private Oficina id_oficina;

    @DBRef
    private PuntoDeAtencion id_punto_de_atencion;

    //public Empleado(){;}

    public Empleado(String id, Usuario id_usuario, String rolEmpleado, Oficina id_oficina,
            PuntoDeAtencion id_punto_de_atencion) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.rolEmpleado = rolEmpleado;
        this.id_oficina = id_oficina;
        this.id_punto_de_atencion = id_punto_de_atencion;
    }


}