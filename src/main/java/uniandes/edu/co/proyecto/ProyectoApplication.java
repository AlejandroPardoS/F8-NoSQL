package uniandes.edu.co.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ProyectoApplication {//implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}

	/*@Override
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

		Usuario usuario = new Usuario();
		usuario.setNumeroDocumento("1019902628");
        usuario.setTipoDocumento("Cedula");
        usuario.setRol("Cliente");
        usuario.setNombre("Alejo");
        usuario.setLogin("alejopardo");
        usuario.setPalabraClave("alejopardo");
        usuario.setNacionalidad("Colombiana");
        usuario.setDireccion("Calle 116");
        usuario.setCorreoElectronico("apardo@gmail.com");
        usuario.setTelefono("3134849896");
        usuario.setCiudad("Bogota");
        usuario.setDepartamento("Cundinamarca");
        usuario.setCodigoPostal("111111");
        usuario.setTipoPersona("Natural");
		usuarioRepository.save(usuario);

		System.out.println("\n USUARIO INSERTADO: \n");
		Usuario nuevoUsuario = usuarioRepository.buscarPorId(usuario.getId());
		System.out.println(nuevoUsuario);
	}*/

}
