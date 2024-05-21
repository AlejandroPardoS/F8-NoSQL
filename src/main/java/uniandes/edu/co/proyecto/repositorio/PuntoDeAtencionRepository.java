package uniandes.edu.co.proyecto.repositorio;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import uniandes.edu.co.proyecto.modelo.PuntoDeAtencion;

public interface PuntoDeAtencionRepository extends MongoRepository<PuntoDeAtencion, ObjectId>{
    
}
