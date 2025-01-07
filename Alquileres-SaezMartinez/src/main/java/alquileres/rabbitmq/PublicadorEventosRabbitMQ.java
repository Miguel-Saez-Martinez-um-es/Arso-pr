package alquileres.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class PublicadorEventosRabbitMQ {

	public void publicarEvento(String routingKey, String mensaje) {
		try (Connection connection = RabbitMQConfig.getConnection(); Channel channel = connection.createChannel()) {

			channel.exchangeDeclare(RabbitMQConfig.EXCHANGE_NAME, "topic", true);
			channel.basicPublish(RabbitMQConfig.EXCHANGE_NAME, routingKey, null, mensaje.getBytes("UTF-8"));
			System.out.println("Evento publicado: " + mensaje + " con clave " + routingKey);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
