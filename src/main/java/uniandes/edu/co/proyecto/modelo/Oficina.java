package uniandes.edu.co.proyecto.modelo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Data
@Document(collection="oficinas")
@ToString
public class Oficina {

    @Id
    private ObjectId id;

    @Field("nombre")
    private String nombre;

    @Field("direccion")
    private String direccion;

    @Field("numeroPuntosAtencion")
    private Integer numeroPuntosAtencion;

    @Field("horarioDeAtencion")
    private String horarioDeAtencion;

    public Oficina() {;}

    public Oficina(ObjectId id, String nombre, String direccion, Integer numeroPuntosAtencion,
            String horarioDeAtencion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.numeroPuntosAtencion = numeroPuntosAtencion;
        this.horarioDeAtencion = horarioDeAtencion;
    }
    
}
