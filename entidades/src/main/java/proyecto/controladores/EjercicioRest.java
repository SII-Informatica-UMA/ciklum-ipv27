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
    public List<EjercicioDTO> obtenerTodosLosEjercicios(UriComponentsBuilder uriBuilder) {
        var ejercicios = servicio.obtenerEjercicios();
        Function<Ejercicio, EjercicioDTO> mapper = (ej -> EjercicioDTO.fromEjercicio(ej,
                ejercicioUriBuilder(uriBuilder.build()),
                ejsUriBuilder(uriBuilder.build())));
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

    public static Function<EjsId, URI> ejsUriBuilder(UriComponents uriComponents) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id -> uriBuilder.path("/ejercicio")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @PostMapping
    public ResponseEntity<EjercicioDTO> aniadirEjercicio(@RequestBody EjercicioDTO ejercicioDTO,
            UriComponentsBuilder uriBuilder) {
        Long id = servicio.aniadirEjercicio(ejercicioDTO.ejercicio());
        return ResponseEntity.created(ejercicioUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @GetMapping("/{idEjercicio}")
    public ResponseEntity<EjercicioDTO> obtenerEjercicio(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        var ejercicio = servicio.obtenerEjercicio(id);
        return ResponseEntity.ok(EjercicioDTO.fromEjercicio(ejercicio, ejercicioUriBuilder(uriBuilder.build()),
                ejsUriBuilder(uriBuilder.build())));
    }

    @PutMapping("/{idEjercicio}")
    public ResponseEntity<Ejercicio> actualizarEjercicio(@PathVariable Long id,
            @RequestBody EjercicioDTO ejercicioDTO) {
        Ejercicio ej = ejercicioDTO.ejercicio();
        ej.setId(id);
        servicio.actualizarEjercicio(ej);
        return ResponseEntity.ok(ej);
    }

    @DeleteMapping("/{idEjercicio}")
    public ResponseEntity<?> eliminarEjercicio(@PathVariable Long id) {
        servicio.eliminarEjercicio(id);
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
