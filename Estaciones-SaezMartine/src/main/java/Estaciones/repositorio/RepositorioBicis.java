package Estaciones.repositorio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import Estaciones.modelo.Bicicleta;

@NoRepositoryBean
public interface RepositorioBicis extends PagingAndSortingRepository<Bicicleta, String>{

    Page<Bicicleta> findByEstacion(String estacion, Pageable pageable);
    
    List<Bicicleta> findByEstacion(String estacion);
 
    Bicicleta findByModelo(String modelo);

}
