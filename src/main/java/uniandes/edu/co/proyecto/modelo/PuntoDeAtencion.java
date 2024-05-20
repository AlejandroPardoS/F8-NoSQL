package uniandes.edu.co.proyecto.modelo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Data
@Document(collection="puntosDeAtencion")
@ToString
public class PuntoDeAtencion {
    
    @Id
    private ObjectId id;

    @Field("latitud")
    private Double latitud;

    @Field("longitud")
    private Double longitud;

    @Field("tipo")
    private String tipo;

    @DBRef
    private ObjectId idOficina;

    public PuntoDeAtencion(ObjectId id, Double latitud, Double longitud, String tipo, ObjectId idOficina) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipo = tipo;
        this.idOficina = idOficina;
    }

    //public PuntoDeAtencion(){;}    
    
}
