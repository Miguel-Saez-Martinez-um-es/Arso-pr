package alquileres.rabbitmq;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Consumidor implements ServletContextListener {

    private Thread consumidorThread;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        consumidorThread = new Thread(() -> {
            try {
                ConsumidorEventosRabbitMQ consumidor = new ConsumidorEventosRabbitMQ();
                consumidor.iniciar();
                System.out.println("Consumidor RabbitMQ iniciado.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error al iniciar el consumidor de RabbitMQ: " + e.getMessage());
            }
        });
        consumidorThread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (consumidorThread != null && consumidorThread.isAlive()) {
            consumidorThread.interrupt();
            System.out.println("Consumidor RabbitMQ detenido.");
        }
    }


}
