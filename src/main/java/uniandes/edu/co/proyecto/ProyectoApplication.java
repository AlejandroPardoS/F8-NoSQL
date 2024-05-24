package uniandes.edu.co.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class ProyectoApplication {// implements CommandLineRunner{

	//@Autowired
	//private CuentaRepository cuentaRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}

	/*@Override
	public void run(String... strings) throws Exception{

		//QUERIES
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date fechaDate = dateFormat.parse("2024-05-01");
		Date fechaDate2 = dateFormat.parse("2024-05-31");
		List<Cuenta> res = cuentaRepository.extractoMensual("1234567990", fechaDate, fechaDate2);
		

		System.out.println("\n EXTRACTO: \n");
		System.out.println(res);
	}*/

}
