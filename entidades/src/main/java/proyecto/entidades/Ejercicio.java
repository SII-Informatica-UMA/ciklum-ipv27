package proyecto.entidades;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
public class Ejercicio {
    @Id
    @GeneratedValue
    private Long id;

    private Long entrenadorId;

    @Column(nullable = false)
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String tipo;
    private String musculosTrabajados;
    private String material;
    private String dificultad;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> multimedia;

    @OneToMany(mappedBy = "ejercicio")
    private List<Ejs> ejs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntrenadorId() {
        return entrenadorId;
    }

    public void setEntrenadorId(Long id) {
        this.entrenadorId = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMusculosTrabajados() {
        return musculosTrabajados;
    }

    public void setMusculosTrabajados(String musculosTrabajados) {
        this.musculosTrabajados = musculosTrabajados;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public List<String> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    public List<Ejs> getEjs() {
        return ejs;
    }

    public void setEjs(List<Ejs> ejs) {
        this.ejs = ejs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Ejercicio other = (Ejercicio) obj;
        return Objects.equals(id, other.id) && Objects.equals(entrenadorId, other.entrenadorId) && Objects.equals(nombre, other.nombre)
                && Objects.equals(descripcion, other.descripcion) &&
                Objects.equals(observaciones, other.observaciones) && Objects.equals(tipo, other.tipo)
                && Objects.equals(musculosTrabajados, other.musculosTrabajados) &&
                Objects.equals(material, other.material) && Objects.equals(dificultad, other.dificultad)
                && Objects.equals(multimedia, other.multimedia)
                && Objects.equals(ejs, other.ejs);
    }

    @Override
    public String toString() {
        return "Ejercicio [id=" + id + " ,entrenadorId=" + entrenadorId + ", nombre=" + nombre + ", descripcion=" + descripcion +
                ", observaciones=" + observaciones + ", tipo=" + tipo + ", musculosTrabajados=" + musculosTrabajados +
                ", material=" + material + ", dificultad=" + dificultad + ", multimedia=" + multimedia + ", ejs=" + ejs
                + "]";
    }

}