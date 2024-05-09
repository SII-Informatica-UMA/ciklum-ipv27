package proyecto.dtos;

import proyecto.entidades.Ejs;
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
    private Long series;
    private Long repeticiones;
    private Long duracionMinutos;
    private EjercicioDTO ejercicio;

    public static EjsDTO fromEjs(Ejs ejs) {
        EjsDTO dto = new EjsDTO();
        dto.setSeries(ejs.getSeries());
        dto.setRepeticiones(ejs.getRepeticiones());
        dto.setDuracionMinutos(ejs.getDuracionMinutos());
        dto.setEjercicio(EjercicioDTO.fromEjercicio(ejs.getEjercicio()));
        return dto;
    }

    public Ejs ejs() {
        Ejs ejs = new Ejs();
        ejs.setSeries(series);
        ejs.setRepeticiones(repeticiones);
        ejs.setDuracionMinutos(duracionMinutos);
        ejs.setEjercicio(ejercicio.ejercicio());
        return ejs;
    }
}


