package pasarela.retrofit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {

	@Bean
	public Retrofit retrofit() {
		return new Retrofit.Builder().baseUrl("http://localhost:5276") // URL del servicio usuarios
				.addConverterFactory(JacksonConverterFactory.create()).client(new OkHttpClient()).build();
	}

	@Bean
	public IServicioVerificacion usuarioService(Retrofit retrofit) {
		return retrofit.create(IServicioVerificacion.class);
	}
}
