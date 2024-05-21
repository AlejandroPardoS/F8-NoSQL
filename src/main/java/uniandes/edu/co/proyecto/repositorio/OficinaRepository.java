package uniandes.edu.co.proyecto.repositorio;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import uniandes.edu.co.proyecto.modelo.Oficina;

public interface OficinaRepository extends MongoRepository<Oficina, ObjectId>{

    @Query("{_id: ?0}")
    @Update("{$inc: {numeroPuntosAtencion: 1}}")
    void incrementarPuntosDeAtencion(ObjectId idOficina);

    @Query("{_id: ?0}")
    @Update("{$inc: {numeroPuntosAtencion: -1}}")
    void disminuirPuntosDeAtencion(ObjectId idOficina);
    
}
