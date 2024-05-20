package uniandes.edu.co.proyecto.repositorio;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import uniandes.edu.co.proyecto.modelo.Cuenta;

public interface CuentaRepository extends MongoRepository<Cuenta, String> {

     @Query("db.cuentes.find({numeroCuenta: ?0})")
    Cuenta buscarPorNumeroCuenta(String numeroCuenta);

}
