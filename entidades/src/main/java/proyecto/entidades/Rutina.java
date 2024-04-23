package proyecto.entidades;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Rutina {

    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String descripcion;
    private String observaciones;

    @OneToMany(mappedBy = "rutina")
    private Ejs[] ejercicios;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre; 
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion; 
    }

    public String getObservaciones(){
        return observaciones;
    }

    public void setObservaciones(String observaciones){
        this.observaciones = observaciones; 
    }

    public Ejs[] getEjercicios(){
        return ejercicios;
    }

    public void setEjercicios(Ejs[] ejercicios){
        this.ejercicios = ejercicios;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }

        if(obj == null){
            return false;
        }

        if(getClass() != obj.getClass()){
            return false;
        }

        Rutina other = (Rutina) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString(){
        return "Rutina [id=" + id + ", nombre=" + nombre + "]";
    }
}