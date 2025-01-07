package alquileres.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {
	public static final String EXCHANGE_NAME = "citybike";
	public static final String URI = "amqps://putfhdao:aUd6BLq1oBSNMLBLisU6t7ocgMxittnG@seal.lmq.cloudamqp.com/putfhdao";

	public static Connection getConnection() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(URI);
		return factory.newConnection();
	}
}
