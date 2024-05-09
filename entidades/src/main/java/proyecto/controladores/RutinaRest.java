package proyecto.controladores;

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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import proyecto.dtos.RutinaDTO;
import proyecto.entidades.*;
import proyecto.servicios.RutinaServicio;
import java.util.function.Function;
import proyecto.servicios.excepciones.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rutina")
public class RutinaRest {
    private RutinaServicio servicio;

    public RutinaRest(RutinaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RutinaDTO> obtenerTodasLasRutinas() {
        var rutinas = servicio.obtenerRutinas();
        Function<Rutina, RutinaDTO> mapper = (r -> RutinaDTO.fromRutina(r));
        return rutinas.stream()
                .map(mapper)
                .toList();
    }

    public static Function<Long, URI> rutinaUriBuilder(UriComponents uriBuilder) {
        ;
        return id -> UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/rutina")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @PostMapping
    public ResponseEntity<RutinaDTO> aniadirRutina(@RequestBody RutinaDTO rutinaDTO, UriComponentsBuilder uriBuilder) {
        Rutina rutina = rutinaDTO.rutina();
        Long id = servicio.aniadirRutina(rutina);
        return ResponseEntity.created(
                rutinaUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @GetMapping("/{idRutina}")
    public ResponseEntity<RutinaDTO> obtenerRutina(@PathVariable Long idRutina) {
        Rutina rutina = servicio.obtenerRutina(idRutina);
        return ResponseEntity.ok(RutinaDTO.fromRutina(rutina));
    }

    @PutMapping("/{idRutina}")
    public ResponseEntity<Rutina> actualizarRutina(@PathVariable Long idRutina, @RequestBody RutinaDTO rutinaDTO) {
        Rutina entidadRutina = rutinaDTO.rutina();
        entidadRutina.setId(idRutina);
        servicio.actualizarRutina(entidadRutina);
        return ResponseEntity.ok(entidadRutina);
    }

    @DeleteMapping("/{idRutina}")
    public ResponseEntity<?> eliminarRutina(@PathVariable Long idRutina) {
        servicio.eliminarRutina(idRutina);
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

}
