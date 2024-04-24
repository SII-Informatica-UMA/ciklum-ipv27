package proyecto.entidades;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Ejercicio {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String tipo;
    private String musculosTrabajados;
    private String material;
    private String dificultad;
    private List<String> multimedia;
    
    @ManyToOne
    @JoinColumn (name = "rutina")
    private Rutina rutina;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public Rutina getRutina() {
        return rutina;
    }
    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ejercicio otherEjercicio = (Ejercicio) obj;
        return Objects.equals(id, otherEjercicio.id);
    }

    @Override
    public String toString() {
        return "Ejercicio [id=" + id + ", nombre=" + nombre + "]";
    }


}