package uniandes.edu.co.proyecto.repositorio;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import uniandes.edu.co.proyecto.modelo.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {

    @Query("{'_id': ?0}")
    Usuario buscarPorId(ObjectId id);

    @Query("{numeroDocumento: ?0}")
    Usuario buscarPorNumDoc(String numeroDocumento);

    
    /*void insertarUsuario(String numeroDocumento, String tipoDocumento, String rol, String nombre, 
                    String login, String palabraClave, String nacionalidad, String direccion, 
                    String correoElectronico, String telefono, String ciudad, String departamento, 
                    String codigoPostal, String tipoPersona);*/

    
}
