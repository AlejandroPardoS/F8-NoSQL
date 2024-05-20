package uniandes.edu.co.proyecto;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import uniandes.edu.co.proyecto.modelo.Cuenta;
import uniandes.edu.co.proyecto.modelo.Usuario;
import uniandes.edu.co.proyecto.repositorio.CuentaRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioRepository;

@SpringBootApplication
public class ProyectoApplication implements CommandLineRunner{

	@Autowired
	private CuentaRepository cuentaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception{

		//QUERIES
		Cuenta res = cuentaRepository.buscarPorNumeroCuenta("1234567903");
		ObjectId id_usuario = res.getId_usuario();
		Usuario res2 = usuarioRepository.buscarPorId(id_usuario);
		System.out.println("\n CUENTA CONSULTADA POR NUMERO DE CUENTA: 1234567903");
		System.out.println(res);
		System.out.println("\n");
		System.out.println("USUARIO ASOCIADO A LA CUENTA CONSULTADA: \n");
		System.out.println(res2);
		System.out.println("\n NACIONALIDAD DEL CLIENTE:");
		System.out.println(res2.getNacionalidad());
	}

}
