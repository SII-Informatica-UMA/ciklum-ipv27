package proyecto.dtos;

import java.net.URI;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;

import proyecto.entidades.Ejercicio;
import proyecto.entidades.Ejs;
import proyecto.entidades.EjsId;
import proyecto.entidades.Rutina;
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
public class EjsDTO {
    private EjsId id; 
    private Long series;
    private Long repeticiones;
    private Long duracionMinutos;
    @JsonProperty("_links")
    private Links links;

    public static EjsDTO fromEjs(Ejs ejs, Function<EjsId, URI> uriBuilder) {
        EjsDTO dto = new EjsDTO();
        dto.setId(ejs.getId());
        dto.setSeries(ejs.getSeries());
        dto.setRepeticiones(ejs.getRepeticiones());
        dto.setDuracionMinutos(ejs.getDuracionMinutos());
        dto.setLinks(
            Links.builder()
                .self(uriBuilder.apply(ejs.getId()))
                .build());
        return dto;
    }

    public Ejs ejs() {
        Ejs ejs = new Ejs();
        ejs.setId(id);
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setId(id.getEjercicioId());
        ejs.setEjercicio(ejercicio);
        Rutina rutina = new Rutina();
        rutina.setId(id.getRutinaId());
        ejs.setRutina(rutina);
        ejs.setSeries(series);
        ejs.setRepeticiones(repeticiones);
        ejs.setDuracionMinutos(duracionMinutos);
        return ejs;
    }
}


