package uniandes.edu.co.proyecto.repositorio;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import uniandes.edu.co.proyecto.modelo.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {

    @Query("{'_id': ?0}")
    Usuario buscarPorId(ObjectId id);

    
}
