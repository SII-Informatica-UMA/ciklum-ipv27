package proyecto.entidades;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Rutina {

    @Id
    @GeneratedValue
    private Long id;
    private Long entrenadorId;


    @Column(nullable = false)
    private String nombre;
    private String descripcion;
    private String observaciones;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.PERSIST)
    private List<Ejs> ejercicios;

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

    public List<Ejs> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<Ejs> ejercicios) {
        this.ejercicios = ejercicios;
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

        Rutina other = (Rutina) obj;
        return Objects.equals(id, other.id) && Objects.equals(entrenadorId, other.entrenadorId) && Objects.equals(nombre, other.nombre)
                && Objects.equals(descripcion, other.descripcion) &&
                Objects.equals(observaciones, other.observaciones) && Objects.equals(ejercicios, other.ejercicios);
    }

    @Override
    public String toString() {
        return "Rutina [id=" + id + " ,entrenadorId=" + entrenadorId + ", nombre=" + nombre + ", descripcion=" + descripcion +
                ", observaciones=" + observaciones + "ejercicios=" + ejercicios + "]";
    }
}