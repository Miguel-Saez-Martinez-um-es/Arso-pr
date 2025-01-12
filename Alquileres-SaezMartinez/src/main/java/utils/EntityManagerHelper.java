package utils;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {
    
	private static EntityManagerFactory entityManagerFactory;

    private static final ThreadLocal<EntityManager> entityManagerHolder;


    static {

        entityManagerHolder = new ThreadLocal<>();

    	
    	 Map<String, String> properties = new HashMap<>();

         // Leer el URL de la base de datos desde las variables de entorno
         String jdbcUrl = System.getenv("DB_URL") != null
                 ? System.getenv("DB_URL")
                 : "jdbc:mysql://localhost:3306/alquileres?serverTimezone=CET";

         properties.put("javax.persistence.jdbc.url", jdbcUrl);
         properties.put("javax.persistence.jdbc.user", "root");
         properties.put("javax.persistence.jdbc.password", "practicas");
         properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
         
        entityManagerFactory = Persistence.createEntityManagerFactory("alquileres", properties);


    }


    public static EntityManager getEntityManager() {

        EntityManager entityManager = entityManagerHolder.get();

        if (entityManager == null || !entityManager.isOpen()) {

            entityManager = entityManagerFactory.createEntityManager();

            entityManagerHolder.set(entityManager);

        }

        return entityManager;

    }


    public static void closeEntityManager() {

        EntityManager entityManager = entityManagerHolder.get();

        if (entityManager != null) {

            entityManagerHolder.set(null);

            entityManager.close();

        }

    }
}
