package Estaciones.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "citybike";
	public static final String QUEUE_BICICLETA_ALQUILADA = "citybike-estaciones";
	public static final String ROUTING_KEY_ALQUILADA = "citybike.alquileres.bicicleta-alquilada";
	public static final String ROUTING_KEY_CONCLUIDO = "citybike.alquileres.bicicleta-alquiler-concluido";

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Queue queueAlquilada() {
		return new Queue(QUEUE_BICICLETA_ALQUILADA);
	}

	@Bean
	public Binding bindingAlquilada(Queue queueAlquilada, TopicExchange exchange) {
		return BindingBuilder.bind(queueAlquilada).to(exchange).with(ROUTING_KEY_ALQUILADA);
	}

	@Bean
	public Binding bindingConcluido(Queue queueAlquilada, TopicExchange exchange) {
		return BindingBuilder.bind(queueAlquilada).to(exchange).with(ROUTING_KEY_CONCLUIDO);
	}
}
