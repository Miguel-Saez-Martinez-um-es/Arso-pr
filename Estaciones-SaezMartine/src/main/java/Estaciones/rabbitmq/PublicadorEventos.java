package Estaciones.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PublicadorEventos {

	private final RabbitTemplate rabbitTemplate;

	private static final String EXHANGE = "citybike";
	
	public PublicadorEventos(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void publicarEventoBicicletaDesactivada(String idBicicleta) {
		String routingKey = "citybike.estaciones.bicicleta-desactivada";
		String mensaje = "{ \"idBicicleta\": \"" + idBicicleta + "\" }";
		rabbitTemplate.convertAndSend(EXHANGE, routingKey, mensaje);
		System.out.println("Evento bicicleta-desactivada publicado: " + mensaje);
	}
}
