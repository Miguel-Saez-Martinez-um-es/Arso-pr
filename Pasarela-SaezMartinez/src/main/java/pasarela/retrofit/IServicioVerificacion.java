package pasarela.retrofit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IServicioVerificacion {

	@POST("/api/usuarios/verificar-credenciales")
	@Headers("Content-Type: application/json")
	Call<List<Map<String, String>>> verificarCredenciales(@Body Map<String, String> credentials);

	@POST("/api/usuarios/verificar-usuario-oauth2")
	@Headers("Content-Type: application/json")
	Call<List<Map<String, String>>> verificarUsuarioOAuth2(@Body Map<String, String> oauth2Request);
}
