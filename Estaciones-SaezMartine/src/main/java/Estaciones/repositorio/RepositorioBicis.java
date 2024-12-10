package Estaciones.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import Estaciones.modelo.Bicicleta;

@NoRepositoryBean
public interface RepositorioBicis extends PagingAndSortingRepository<Bicicleta, String>{

    Page<Bicicleta> findByEstacion(String estacion, Pageable pageable);

}
