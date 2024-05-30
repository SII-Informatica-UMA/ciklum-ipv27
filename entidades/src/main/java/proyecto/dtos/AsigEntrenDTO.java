package proyecto.dtos;

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
public class AsigEntrenDTO {
    private Long idEntrenador;
    private Long idCliente;
    private String especialidad;
    private Long id;
}
