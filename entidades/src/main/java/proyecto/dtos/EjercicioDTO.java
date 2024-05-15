package proyecto.dtos;

import java.util.List;
import proyecto.entidades.Ejercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjercicioDTO {
    private Long id;
    private Long entrenadorId;
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String tipo;
    private String musculosTrabajados;
    private String material;
    private String dificultad;
    private List<String> multimedia;

    public static EjercicioDTO fromEjercicio(Ejercicio ejercicio) {
        var dto = new EjercicioDTO();
        dto.setId(ejercicio.getId());
        dto.setEntrenadorId(ejercicio.getEntrenadorId());
        dto.setNombre(ejercicio.getNombre());
        dto.setDescripcion(ejercicio.getDescripcion());
        dto.setObservaciones(ejercicio.getObservaciones());
        dto.setTipo(ejercicio.getTipo());
        dto.setMusculosTrabajados(ejercicio.getMusculosTrabajados());
        dto.setMaterial(ejercicio.getMaterial());
        dto.setDificultad(ejercicio.getDificultad());
        dto.setMultimedia(ejercicio.getMultimedia());
        return dto;
    }
    
    public Ejercicio ejercicio() {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setId(id);
        ejercicio.setEntrenadorId(entrenadorId);
        ejercicio.setNombre(nombre);
        ejercicio.setDescripcion(descripcion);
        ejercicio.setObservaciones(observaciones);
        ejercicio.setTipo(tipo);
        ejercicio.setMusculosTrabajados(musculosTrabajados);
        ejercicio.setMaterial(material);
        ejercicio.setDificultad(dificultad);
        ejercicio.setMultimedia(multimedia);
        return ejercicio;
    }
}
