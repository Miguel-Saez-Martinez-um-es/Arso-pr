package alquileres.servicio;

import java.util.List;

import retrofit.BicicletaPOJO;
import retrofit.EstacionPOJO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IServicioEstacion {

	@GET("estaciones")
	Call<List<EstacionPOJO>> getEstaciones();
	
	@GET("estaciones/{id}")
	Call<EstacionPOJO> getEstacion(@Path("id") String id);

	@PUT("estaciones/{idEstacion}/estacionar/{idBicicleta}")
	Call<BicicletaPOJO> estacionarBicicleta(@Path("idBicicleta") String idBicicleta, @Path("idEstacion") String idEstacion);

}
