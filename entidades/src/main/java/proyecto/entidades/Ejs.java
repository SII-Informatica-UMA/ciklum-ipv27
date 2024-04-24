package proyecto.entidades;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Ejs {

	@EmbeddedId
	EjsId id;

	@ManyToOne
	@MapsId("ejercicioId")
	@JoinColumn(name = "ejercicio_id")
	Ejercicio ejercicio;

	@ManyToOne
	@MapsId("rutinaId")
	@JoinColumn(name = "rutina_id")
	Rutina rutina;

	@Column(nullable = false)
	Long series;
	@Column(nullable = false)
	Long repeticiones;
	@Column(nullable = false)
	Long duracionMinutos;

	public Ejs(EjsId id, Ejercicio ejercicio, Rutina rutina, Long series, Long repeticiones, Long duracionMinutos) {
		this.id = id;
		this.ejercicio = ejercicio;
		this.rutina = rutina;
		this.series = series;
		this.repeticiones = repeticiones;
		this.duracionMinutos = duracionMinutos;
	}

	public EjsId getId() {
		return id;
	}

	public void setId(EjsId id) {
		this.id = id;
	}

	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Rutina getRutina() {
		return rutina;
	}

	public void setRutina(Rutina rutina) {
		this.rutina = rutina;
	}

	public Long getSeries() {
		return series;
	}

	public void setSeries(Long series) {
		this.series = series;
	}

	public Long getRepeticiones() {
		return repeticiones;
	}

	public void setRepeticiones(Long repeticiones) {
		this.repeticiones = repeticiones;
	}

	public Long getDuracionMinutos() {
		return duracionMinutos;
	}

	public void setDuracionMinutos(Long duracionMinutos) {
		this.duracionMinutos = duracionMinutos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(duracionMinutos, id, repeticiones, rutina, series);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ejs other = (Ejs) obj;
		return Objects.equals(duracionMinutos, other.duracionMinutos) && Objects.equals(id, other.id)
				&& Objects.equals(repeticiones, other.repeticiones) && Objects.equals(rutina, other.rutina)
				&& Objects.equals(series, other.series) && Objects.equals(ejercicio, other.ejercicio);
	}

	@Override
	public String toString() {
		return "Ejs [id=" + id + ", rutina=" + rutina + ", ejercicio=" + ejercicio + ", series=" + series
				+ ", repeticiones=" + repeticiones
				+ ", duracionMinutos=" + duracionMinutos + "]";
	}

}