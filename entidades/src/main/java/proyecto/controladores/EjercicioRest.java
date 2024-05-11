package proyecto.controladores;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import proyecto.dtos.EjercicioDTO;
import proyecto.entidades.*;
import proyecto.servicios.RutinaServicio;
import proyecto.servicios.excepciones.*;

@RestController
@RequestMapping("/ejercicio")
public class EjercicioRest {

    private RutinaServicio servicio;

    public EjercicioRest(RutinaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<EjercicioDTO> obtenerTodosLosEjercicios() {
        var ejercicios = servicio.obtenerEjercicios();
        Function<Ejercicio, EjercicioDTO> mapper = (ej -> EjercicioDTO.fromEjercicio(ej));
        return ejercicios.stream()
                .map(mapper)
                .toList();
    }

    public static Function<Long, URI> ejercicioUriBuilder(UriComponents uriComponents) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id -> uriBuilder.path("/ejercicio")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }


    @PostMapping
    public ResponseEntity<EjercicioDTO> aniadirEjercicio(@RequestBody EjercicioDTO ejercicioDTO,
            UriComponentsBuilder uriBuilder) {
        Ejercicio ejercicio = servicio.aniadirEjercicio(ejercicioDTO.ejercicio());
        EjercicioDTO ejercicioCreadoDTO = EjercicioDTO.fromEjercicio(ejercicio);
        URI location = ejercicioUriBuilder(uriBuilder.build()).apply(ejercicio.getId());
        return ResponseEntity.created(location).body(ejercicioCreadoDTO);

    }

    @GetMapping("/{idEjercicio}")
    public ResponseEntity<EjercicioDTO> obtenerEjercicio(@PathVariable Long idEjercicio) {
        var ejercicio = servicio.obtenerEjercicio(idEjercicio);
        return ResponseEntity.ok(EjercicioDTO.fromEjercicio(ejercicio));
    }

    @PutMapping("/{idEjercicio}")
    public ResponseEntity<EjercicioDTO> actualizarEjercicio(@PathVariable Long idEjercicio,
            @RequestBody EjercicioDTO ejercicioDTO) {
        Ejercicio ej = ejercicioDTO.ejercicio();
        ej.setId(idEjercicio);
        servicio.actualizarEjercicio(ej);
        return ResponseEntity.ok(EjercicioDTO.fromEjercicio(ej));
    }

    @DeleteMapping("/{idEjercicio}")
    public ResponseEntity<?> eliminarEjercicio(@PathVariable Long idEjercicio) {
        servicio.eliminarEjercicio(idEjercicio);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {
    }

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public void existente() {
    }

    @ExceptionHandler(EjercicioNoEliminadoException.class)
    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    public void noEliminado() {

    }

}
