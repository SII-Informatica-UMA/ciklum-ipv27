package proyecto.dtos;

import java.time.LocalDateTime;

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
public class ClienteDTO {
    private Long id;
    private Long usuarioId;
   
    private String telefono;
    private String direccion;
    private String dni;
    private LocalDateTime fechaNacimiento;
    
}
