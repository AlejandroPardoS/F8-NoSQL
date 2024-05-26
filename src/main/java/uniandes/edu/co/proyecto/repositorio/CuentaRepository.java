package uniandes.edu.co.proyecto.repositorio;


import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
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

    @Query("{tipoCuenta: ?0}")
    List<Cuenta> buscarPorTipoCuenta(String tipoCuenta);

    @Query("{tipoCuenta: ?0, id_usuario: ?1}")
    List<Cuenta> buscarPorTipoCuentaYUsuario(String tipoCuenta, ObjectId idUsuario);

    @Query("{saldo: { $gte: ?0, $lte: ?1 }}")
    List<Cuenta> buscarPorRangoSaldo(Double saldoMin, Double saldoMax);

    @Query("{saldo: { $gte: ?0, $lte: ?1 }, id_usuario: ?2 }")
    List<Cuenta> buscarPorRangoSaldoYUsuario(Double saldoMin, Double saldoMax, ObjectId idUsuario);

    @Query("{id_usuario: ?0}")
    List<Cuenta> buscarPorIdUsuario(ObjectId idUsuario);

    @Query("{fechaCreacion: { $gte: ?0, $lte: ?1 }}")
    List<Cuenta> buscarPorRangoFecha(Date fechaInicio, Date fechaFin);

    @Query("{fechaCreacion: { $gte: ?0, $lte: ?1 }, id_usuario: ?2}")
    List<Cuenta> buscarPorRangoFechaYUsuario(Date fechaInicio, Date fechaFin, ObjectId idUsuario);

    @Aggregation(pipeline = {
        "{ $match: { 'numeroCuenta': ?0 } }, " + 
        "{ $unwind: '$operacionesBancarias' }, " + 
        "{ $match: { 'operacionesBancarias.fecha': { $gte: ?1, $lte: ?2 } } }, " +
        "{ $group: { " +
            "'_id': '$_id', " +
            "'numeroCuenta': { $first: '$numeroCuenta' }, " +
            "'id_usuario': { $first: '$id_usuario' }, " +
            "'tipoCuenta': { $first: '$tipoCuenta' }, " +
            "'estadoCuenta': { $first: '$estadoCuenta' }, " +
            "'saldo': { $first: '$saldo' }, " +
            "'fechaCreacion': { $first: '$fechaCreacion' }, " +
            "'operacionesBancarias': { $push: '$operacionesBancarias' } " +
        "} }"
    })
    Cuenta extractoMensual(String numeroCuenta, Date inicioMes, Date finMes);

}