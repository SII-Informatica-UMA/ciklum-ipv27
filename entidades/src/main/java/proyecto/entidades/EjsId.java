import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EjsId implements Serializable{
	
	@Column(name="ejercicio_id")
	Long ejercicioId;
	
	@Column(name="rutina_id")
	Long rutinaId;
	
	public EjsId(Long ejercicioId, Long rutinaId) {
		this.ejercicioId = ejercicioId;
		this.rutinaId = rutinaId;
	}

	public Long getEjercicioId() {
		return ejercicioId;
	}

	public void setEjercicioId(Long ejercicioId) {
		this.ejercicioId = ejercicioId;
	}

	public Long getRutinaId() {
		return rutinaId;
	}

	public void setRutinaId(Long rutinaId) {
		this.rutinaId = rutinaId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ejercicioId, rutinaId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EjsId other = (EjsId) obj;
		return Objects.equals(ejercicioId, other.ejercicioId) && Objects.equals(rutinaId, other.rutinaId);
	}

	@Override
	public String toString() {
		return "EjsId [ejercicioId=" + ejercicioId + ", rutinaId=" + rutinaId + "]";
	}
	
	
}
