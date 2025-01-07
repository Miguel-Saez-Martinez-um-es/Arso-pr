package Estaciones.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.servicio.IServicioEstacion;


@Service
public class ConsumidorEventos {

	// Claves de enrutamiento estáticas para comparar
	private static final String EVENTO_BICICLETA_ALQUILADA = "citybike.alquileres.bicicleta-alquilada";
	private static final String EVENTO_BICICLETA_ALQUILER_CONCLUIDO = "citybike.alquileres.bicicleta-alquiler-concluido";

	private final IServicioEstacion servicio;
	private final ObjectMapper objectMapper;

	@Autowired
	public ConsumidorEventos(IServicioEstacion servicioEstacion, ObjectMapper objectMapper) {
		this.servicio = servicioEstacion;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "citybike-estaciones")
	public void procesarEvento(String mensaje, @Header("amqp_receivedRoutingKey") String routingKey) {
		System.out.println("Evento recibido con clave de enrutamiento: " + routingKey);
		try {
			switch (routingKey) {
			case EVENTO_BICICLETA_ALQUILADA:
				procesarEventoBicicletaAlquilada(mensaje);
				break;
			case EVENTO_BICICLETA_ALQUILER_CONCLUIDO:
				procesarEventoBicicletaAlquilerConcluido(mensaje);
				break;
			default:
				System.err.println("Clave de enrutamiento desconocida: " + routingKey);
			}
		} catch (Exception e) {
			System.err.println("Error procesando el mensaje de RabbitMQ: " + e.getMessage());
		}
	}

	private void procesarEventoBicicletaAlquilada(String mensaje) {
		try {
			if (mensaje == null ) {
				throw new IllegalArgumentException("El evento bicicleta-alquilada no contiene un idBicicleta válido.");
			}
			System.out.println();
			Bicicleta b = servicio.getBicicleta(mensaje);
			Estacion e = servicio.getEstacion(b.getEstacion());
			
			System.out.println("\t"+b);
			System.out.println("\t"+e.getNombre() + " " + servicio.bicicletasEnEstacion(e.getNombre())+ "/" + e.getCapacidad());
			servicio.alquilarBicicleta(mensaje);
			System.out.println("\t"+b);
			System.out.println("\t"+e.getNombre() + " " + servicio.bicicletasEnEstacion(e.getNombre())+ "/" + e.getCapacidad());
			System.out.println();

			System.out.println("Procesado evento bicicleta-alquilada para bicicleta: " +mensaje);
		} catch (Exception e) {
			System.err.println("Error procesando el evento bicicleta-alquilada: " + e.getMessage());
		}
	}

	private void procesarEventoBicicletaAlquilerConcluido(String mensaje) {
		try {
			// Parsear el mensaje a EventoBicicletaAlquilerConcluido
			EventoBicicletaAlquilerConcluido evento = objectMapper.readValue(mensaje,
					EventoBicicletaAlquilerConcluido.class);

			// Validación adicional
			if (evento.getIdBicicleta() == null || evento.getIdEstacion() == null || evento.getIdBicicleta().isEmpty()
					|| evento.getIdEstacion().isEmpty()) {
				throw new IllegalArgumentException("El evento bicicleta-alquiler-concluido no tiene datos válidos.");
			}

			Bicicleta b = servicio.getBicicleta(evento.getIdBicicleta());
			Estacion e = servicio.getEstacion(evento.getIdEstacion());
			
			// Llamar al método del servicio
			System.out.println();
			System.out.println("\t"+b);
			System.out.println("\t"+e.getNombre() + " " + servicio.bicicletasEnEstacion(e.getNombre())+ "/" + e.getCapacidad());
			servicio.estacionarBicicleta(evento.getIdBicicleta(), evento.getIdEstacion());
			System.out.println("\t"+b);
			System.out.println("\t"+e.getNombre() + " " + servicio.bicicletasEnEstacion(e.getNombre())+ "/" + e.getCapacidad());

			System.out.println(
					"Procesado evento bicicleta-alquiler-concluido para bicicleta: " + evento.getIdBicicleta());
		} catch (Exception e) {
			System.err.println("Error procesando el evento bicicleta-alquiler-concluido: " + e.getMessage());
		}
	}
}
