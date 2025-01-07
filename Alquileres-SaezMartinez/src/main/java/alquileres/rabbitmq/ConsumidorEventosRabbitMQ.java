package alquileres.rabbitmq;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import alquileres.servicio.IServicioAlquileres;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class ConsumidorEventosRabbitMQ {

	private static final String QUEUE_NAME = "citybike-alquileres";
	private static final String EXCHANGE_NAME = "citybike";
	private static final String ROUTING_KEY = "citybike.estaciones.bicicleta-desactivada";

	private IServicioAlquileres servicio = FactoriaServicios.getServicio(IServicioAlquileres.class);

	public void iniciar() throws Exception {
		Connection connection = RabbitMQConfig.getConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		channel.basicConsume(QUEUE_NAME, false, "alquileres-consumidor", new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				try {
					tratarMensaje(envelope, body);
					channel.basicAck(envelope.getDeliveryTag(), false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void tratarMensaje(Envelope envelope, byte[] body)
			throws IOException, RepositorioException, EntidadNoEncontrada {
		String mensaje = new String(body, "UTF-8");
		System.out.println("Evento recibido: " + mensaje);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> eventoMap = objectMapper.readValue(mensaje, Map.class); 
																					

		if (!eventoMap.containsKey("idBicicleta") || eventoMap.get("idBicicleta") == null) {
			throw new IllegalArgumentException("El evento recibido no contiene un idBicicleta válido.");
		}

		String idBicicleta = eventoMap.get("idBicicleta").toString();

		System.out.println("Procesando evento de desactivacion de bicicleta para : " + idBicicleta);
		servicio.bicicletaDesactivada(idBicicleta);
		System.out.println("Reservas activas terminadas para la bicicleta: " + idBicicleta);
	}

}
