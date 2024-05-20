package uniandes.edu.co.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.ToString;

@Data
@Document(collection="usuarios")
@ToString
public class Usuario {
    
    @Id
    private String id;

    @Field("numeroDocumento")
    private String numeroDocumento;
    @Field("tipoDocumento")
    private String tipoDocumento;
    @Field("rol")
    private String rol;
    @Field("nombre")
    private String nombre;
    @Field("login")
    private String login;
    @Field("palabraClave")
    private String palabraClave;
    @Field("nacionalidad")
    private String nacionalidad;
    @Field("direccion")
    private String direccion;
    @Field("correoElectronico")
    private String correoElectronico;
    @Field("telefono")
    private String telefono;
    @Field("ciudad")
    private String ciudad;
    @Field("departamento")
    private String departamento;
    @Field("codigoPostal")
    private String codigoPostal;
    @Field("tipoPersona")
    private String tipoPersona;
   

    public Usuario(String id, String numeroDocumento, String tipoDocumento, String rol, String nombre, String login,
            String palabraClave, String nacionalidad, String direccion, String correoElectronico, String telefono,
            String ciudad, String departamento, String codigoPostal, String tipoPersona) {
        this.id = id;
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.rol = rol;
        this.nombre = nombre;
        this.login = login;
        this.palabraClave = palabraClave;
        this.nacionalidad = nacionalidad;
        this.direccion = direccion;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.ciudad = ciudad;
        this.departamento = departamento;
        this.codigoPostal = codigoPostal;
        this.tipoPersona = tipoPersona;
    }
    //public Usuario(){}
    
}
