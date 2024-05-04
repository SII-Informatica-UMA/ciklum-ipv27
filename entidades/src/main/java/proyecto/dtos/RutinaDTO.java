package proyecto.dtos;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto.entidades.EjsId;
import proyecto.entidades.Rutina;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutinaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String observaciones;
    private List<EjsDTO> ejercicios; 
    @JsonProperty("_links")
    private Links links;


    public static RutinaDTO fromRutina(Rutina rutina, Function<Long, URI> uriBuilder, Function<EjsId, URI> ejsUriBuilder) {
        var dto = new RutinaDTO();
        dto.setId(rutina.getId());
        dto.setNombre(rutina.getNombre());
        dto.setDescripcion(rutina.getDescripcion());
        dto.setObservaciones(rutina.getObservaciones());
        dto.setLinks(
                Links.builder()
                        .self(uriBuilder.apply(rutina.getId()))
                        .build());
        dto.setEjercicios(
                rutina.getEjercicios().stream()
                        .map(ejs -> EjsDTO.fromEjs(ejs,  ejsUriBuilder))
                        .collect(Collectors.toList())
        );
        return dto;
    }

    public Rutina rutina() {
        Rutina rutina = new Rutina();
        rutina.setId(id);
        rutina.setNombre(nombre);
        rutina.setDescripcion(descripcion);
        rutina.setObservaciones(observaciones);
        rutina.setEjercicios(Optional.ofNullable(ejercicios)
                .orElse(Collections.emptyList())
                .stream()
                .map(EjsDTO::ejs)
                .collect(Collectors.toList()));
        return rutina;
    }
}
