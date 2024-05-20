package uniandes.edu.co.proyecto.modelo;

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
    private String id;

    @Field("latitud")
    private Double latitud;

    @Field("longitud")
    private Double longitud;

    @Field("tipo")
    private String tipo;

    @DBRef
    private Oficina idOficina;

    public PuntoDeAtencion(String id, Double latitud, Double longitud, String tipo, Oficina idOficina) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipo = tipo;
        this.idOficina = idOficina;
    }

    //public PuntoDeAtencion(){;}    
    
}
