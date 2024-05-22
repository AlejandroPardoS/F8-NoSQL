package uniandes.edu.co.proyecto.repositorio;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;


import uniandes.edu.co.proyecto.modelo.Cuenta;

public interface CuentaRepository extends MongoRepository<Cuenta, ObjectId> {

    @Query("{numeroCuenta: ?0}")
    Cuenta buscarPorNumeroCuenta(String numeroCuenta);

    
    @Query("{_id: ?0}")
    @Update("{$set:{'estadoCuenta': ?1}}")
    void actualizarEstadoCuenta(ObjectId idCuenta, String nuevoEstado);


    @Query("{_id: ?0}")
    @Update("{$set:{'tipoCuenta': ?1}}")
    void actualizarTipoCuenta(ObjectId idCuenta, String tipoCuenta);

    @Query("{_id: ?0}")
    @Update("{$set:{'saldo': ?1}}")
    void actualizarSaldo(ObjectId idCuenta, Double saldo);

}
