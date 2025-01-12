package alquileres.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {
	public static final String EXCHANGE_NAME = "citybike";
	public static final String URI = System.getenv("RABBITMQ_URL") != null 
		    ? System.getenv("RABBITMQ_URL") 
		    	    : "amqp://guest:guest@localhost:5672";

	public static Connection getConnection() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(URI);
		return factory.newConnection();
	}
}
