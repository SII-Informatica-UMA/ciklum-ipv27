package proyecto.dtos;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import proyecto.entidades.Ejercicio;
import proyecto.entidades.Ejs;
import proyecto.entidades.EjsId;
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
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String tipo;
    private String musculosTrabajados;
    private String material;
    private String dificultad;
    private List<String> multimedia;
    private List<EjsDTO> ejs;
    @JsonProperty("_links")
    private Links links;

    public static EjercicioDTO fromEjercicio(Ejercicio ejercicio, Function<Long, URI> uriBuilder, Function<EjsId, URI> ejsUriBuilder) {
        var dto = new EjercicioDTO();
        dto.setId(ejercicio.getId());
        dto.setNombre(ejercicio.getNombre());
        dto.setDescripcion(ejercicio.getDescripcion());
        dto.setObservaciones(ejercicio.getObservaciones());
        dto.setTipo(ejercicio.getTipo());
        dto.setMusculosTrabajados(ejercicio.getMusculosTrabajados());
        dto.setMaterial(ejercicio.getMaterial());
        dto.setDificultad(ejercicio.getDificultad());
        dto.setLinks(
            Links.builder()
                .self(uriBuilder.apply(ejercicio.getId()))
                .build());
        dto.setMultimedia(ejercicio.getMultimedia());
        dto.setEjs(
            ejercicio.getEjs().stream()
                .map(e -> EjsDTO.fromEjs(e, ejsUriBuilder))
                .collect(Collectors.toList())
        );
        return dto;
    }
    
    public Ejercicio ejercicio() {
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setId(id);
        ejercicio.setNombre(nombre);
        ejercicio.setDescripcion(descripcion);
        ejercicio.setObservaciones(observaciones);
        ejercicio.setTipo(tipo);
        ejercicio.setMusculosTrabajados(musculosTrabajados);
        ejercicio.setMaterial(material);
        ejercicio.setDificultad(dificultad);
        ejercicio.setMultimedia(multimedia);
        
        // Map EjsDTOs to Ejs entities
        List<Ejs> ejsList = Optional.ofNullable(ejs)
            .orElse(Collections.emptyList())
            .stream()
            .map(EjsDTO::ejs)
            .collect(Collectors.toList());

        ejercicio.setEjs(ejsList);
        
        return ejercicio;
    }
}
