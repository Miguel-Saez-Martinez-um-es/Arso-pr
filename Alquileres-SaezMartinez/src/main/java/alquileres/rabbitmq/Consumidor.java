package alquileres.rabbitmq;

import java.io.IOException;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Consumidor {

	public static void main(String[] args) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri("amqps://putfhdao:aUd6BLq1oBSNMLBLisU6t7ocgMxittnG@seal.lmq.cloudamqp.com/putfhdao");

		Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();

		String QUEUE_NAME = "citybike-alquileres";
		String EXCHANGE_NAME = "citybike";
		String ROUTING_KEY = "citybike.estaciones.bicicleta-desactivada";

		boolean durable = true;
		boolean exclusive = false;
		boolean autodelete = false;
		Map<String, Object> properties = null; // sin propiedades
		channel.queueDeclare(QUEUE_NAME, durable, exclusive, autodelete, properties);

		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		boolean autoAck = false;
		String etiquetaConsumidor = "arso-consumidor";

		channel.basicConsume(QUEUE_NAME, autoAck, etiquetaConsumidor, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {

				String routingKey = envelope.getRoutingKey();
				String contentType = properties.getContentType();
				long deliveryTag = envelope.getDeliveryTag();

				String contenido = new String(body);

				System.out.println(contenido);
				// Confirma el procesamiento
				channel.basicAck(deliveryTag, false);
			}
		});
		System.out.println("consumidor esperando...");
	}
}
