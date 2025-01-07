package retrofit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BicicletaPOJO {
	
	private String codigo;
	
	private String modelo;
	
	private String estacion;



	public BicicletaPOJO() {

	}

	public BicicletaPOJO(String codigo, String modelo, String estacion) {
		this.codigo = codigo;
		this.modelo = modelo;
		this.estacion = estacion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getEstacion() {
		return estacion;
	}

	public void setEstacion(String estacion) {
		this.estacion = estacion;
	}
}
