package Estaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"estaciones", "rabbitMQ"})
public class EstacionesApp {

	public static void main(String[] args) {
		SpringApplication.run(EstacionesApp.class, args);
	}
}
