package retrofit;

import alquileres.servicio.IServicioEstacion;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Programa {

	public static void main(String[] args) throws Exception {

		String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWd1ZWwtU2Flei1NYXJ0aW5lei11bS1lcyIsImV4cCI6MTczNDQ2NzczOCwicm9sIjoiZ2VzdG9yIn0.9quH7K9fHZ4_q651AMgLolV3ic984pyPfm3SEiqwMy0";

		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
			Request originalRequest = chain.request();
			Request newRequest = originalRequest.newBuilder().header("Authorization", "Bearer " + jwtToken) // Añadir el
																											// encabezado
																											// de
					// autenticación
					.build();
			return chain.proceed(newRequest);
		}).build();

		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/")
				.addConverterFactory(JacksonConverterFactory.create()).client(client).build();

		IServicioEstacion service = retrofit.create(IServicioEstacion.class);

		String idEstacion = "Estacion1";
		String idBicicletaEnEstacion1 = "Modelo1";
		String idEstacion4 = "Estacion4";		
		
		
		
		
		
		Call<EstacionPOJO> call = service.getEstacion(idEstacion);
		Call<EstacionPOJO> call2 = service.getEstacion(idEstacion4);

		Response<EstacionPOJO> response = call.execute();
		Response<EstacionPOJO> response2 = call2.execute();

		if (response.isSuccessful()) {
			if (response2.isSuccessful()) {
				EstacionPOJO estacion = response.body();
				EstacionPOJO estacion2 = response2.body();
				System.out.println(estacion + " / " + service.getEstacion(idEstacion).execute().body().getHuecos());
				System.out.println(estacion2);
			} else {
				System.out.println("Error: Código HTTP " + response2.code());
				if (response2.errorBody() != null) {
					System.out.println("Cuerpo del error: " + response2.errorBody().string());
				}
			}
		} else {
			System.out.println("Error: Código HTTP " + response.code());
			if (response.errorBody() != null) {
				System.out.println("Cuerpo del error: " + response.errorBody().string());
			}
		}

		System.out.println();
		System.out.println("Intentamos estacionar la bicicleta de la estacion 1 en la estacion 4");
		Call<BicicletaPOJO> callB = service.estacionarBicicleta(idBicicletaEnEstacion1, idEstacion4);
		Response<BicicletaPOJO> responseB = callB.execute();

		if (responseB.isSuccessful()) {
		} else {
			System.out.println("Error: Código HTTP " + responseB.code());
			if (responseB.errorBody() != null) {
				System.out.println("Cuerpo del error: " + responseB.errorBody().string());
			}
		}

		System.out.println();
		call = service.getEstacion(idEstacion);
		call2 = service.getEstacion(idEstacion4);

		response = call.execute();
		response2 = call2.execute();

		if (response.isSuccessful()) {
			if (response2.isSuccessful()) {
				EstacionPOJO estacion = response.body();
				EstacionPOJO estacion2 = response2.body();
				System.out.println(estacion);
				System.out.println(estacion2);
			} else {
				System.out.println("Error: Código HTTP " + response2.code());
				if (response2.errorBody() != null) {
					System.out.println("Cuerpo del error: " + response2.errorBody().string());
				}
			}
		} else {
			System.out.println("Error: Código HTTP " + response.code());
			if (response.errorBody() != null) {
				System.out.println("Cuerpo del error: " + response.errorBody().string());
			}
		}

		System.out.println();
		System.out.println("Intentamos devolver la bicicleta que estaba en la estacion 1 a la estacion 1");
		callB = service.estacionarBicicleta(idBicicletaEnEstacion1, idEstacion);
		responseB = callB.execute();

		if (responseB.isSuccessful()) {
		} else {
			System.out.println("Error: Código HTTP " + responseB.code());
			if (responseB.errorBody() != null) {
				System.out.println("Cuerpo del error: " + responseB.errorBody().string());
			}
		}

		System.out.println();
		call = service.getEstacion(idEstacion);
		call2 = service.getEstacion(idEstacion4);

		response = call.execute();
		response2 = call2.execute();

		if (response.isSuccessful()) {
			if (response2.isSuccessful()) {
				EstacionPOJO estacion = response.body();
				EstacionPOJO estacion2 = response2.body();
				System.out.println(estacion);
				System.out.println(estacion2);
			} else {
				System.out.println("Error: Código HTTP " + response2.code());
				if (response2.errorBody() != null) {
					System.out.println("Cuerpo del error: " + response2.errorBody().string());
				}
			}
		} else {
			System.out.println("Error: Código HTTP " + response.code());
			if (response.errorBody() != null) {
				System.out.println("Cuerpo del error: " + response.errorBody().string());
			}
		}

	}
}
