package proyecto.servicios;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import proyecto.dtos.EntrenadorDTO;
import proyecto.entidades.*;
import proyecto.repositorios.*;
import proyecto.seguridad.SecurityConfguration;
import proyecto.servicios.excepciones.*;

@Service
@Transactional
public class RutinaServicio {

    private RutinaRepository rutinaRepo;
    private EjercicioRepository ejercicioRepo;
    private EjsRepository ejsRepo;

    @Autowired
	private RestTemplate restTemplate;

	private int port = 8080;

	@Value(value = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzE1OTU2ODkwLCJleHAiOjE3MjU5NTY4OTB9.fnHLue1zBs_qw86FL3XYySlmSqgE8Mr9McLx2Ycn2JJapV3QjMg0Y7LRC9f8OQadS8cp_9jV5BdqfoI_gEYECA")
	private String jwtToken;

    private URI uri(String scheme, String host, int port, Long entrenadorId, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		if (entrenadorId != null) {
            ub = ub.path("/"+entrenadorId.toString());
		}
		return ub.build();
	}

    private RequestEntity<Void> get(String scheme, String host, int port, String path, String jwtToken, Long entrenadorId) {
		URI uri = uri(scheme, host, port, entrenadorId, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
				.build();
		return peticion;
	}

    private void validarEntrenador(Long entrenadorId) {
        Optional<UserDetails> user = SecurityConfguration.getAuthenticatedUser();
        Long usuarioId=Long.valueOf(user.get().getUsername());
        var peticion = get("http", "localhost", port, "/entrenador", jwtToken, entrenadorId);
        try {
            var respuesta = restTemplate.exchange(peticion,new ParameterizedTypeReference<EntrenadorDTO>() {});
            EntrenadorDTO entrenador = respuesta.getBody();
            if (!entrenador.getUsuarioId().equals(usuarioId)) {
                throw new UsuarioException("El usuario " + usuarioId + " no tiene acceso a los objetos del entrenador " + entrenadorId);
            }
        } catch(HttpClientErrorException e){
            if(e.getStatusCode().value()==404){
                throw new EntidadNoEncontradaException("El entrenador " + entrenadorId + " no existe");
            }
        }

        
        
    }

    public RutinaServicio(RutinaRepository rutinaRepo,
            EjercicioRepository ejercicioRepo, EjsRepository ejsRepo) {
        this.rutinaRepo = rutinaRepo;
        this.ejercicioRepo = ejercicioRepo;
        this.ejsRepo = ejsRepo;
    }

    public List<Rutina> obtenerRutinas(Long entrenadorId) {
        validarEntrenador(entrenadorId);
        return rutinaRepo.findAllByEntrenadorId(entrenadorId);
    }

    public Rutina aniadirRutina(Rutina rutina) {
        validarEntrenador(rutina.getEntrenadorId());
        List<Ejs> l = rutina.getEjercicios();
        rutina.setEjercicios(null);
        rutina = rutinaRepo.save(rutina);
        for (Ejs e : l) {
            e.setId(new EjsId(e.getEjercicio().getId(), rutina.getId()));
            e.setRutina(rutina);
            ejsRepo.save(e);
        }
        rutina.setEjercicios(l);
        return rutinaRepo.save(rutina); 
    }

    public Rutina obtenerRutina(Long id) {
        Optional<Rutina> optionalRutina = rutinaRepo.findById(id);
        if (optionalRutina.isPresent()) {
            Rutina ans = optionalRutina.get();
            validarEntrenador(ans.getEntrenadorId());
            return ans;
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada");
        }
    }

    public void actualizarRutina(Rutina entidadRutina) {
        if (rutinaRepo.existsById(entidadRutina.getId())) {
            validarEntrenador(entidadRutina.getEntrenadorId());
            List<Ejs> l = entidadRutina.getEjercicios();
            entidadRutina.setEjercicios(null);
            entidadRutina = rutinaRepo.save(entidadRutina);
            for (Ejs e : l) {
                e.setId(new EjsId(e.getEjercicio().getId(), entidadRutina.getId()));
                e.setRutina(entidadRutina);
                ejsRepo.save(e);
            }
            entidadRutina.setEjercicios(l);
            entidadRutina = rutinaRepo.save(entidadRutina);
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + entidadRutina.getId() + " no fue encontrada");
        }
    }

    public void eliminarRutina(Long id) {
        if (rutinaRepo.existsById(id)) {
            Rutina rutina = rutinaRepo.getReferenceById(id);
            validarEntrenador(rutina.getEntrenadorId());
            List<Ejs> l = rutina.getEjercicios();
            for (Ejs e : l) {
                ejsRepo.deleteById(e.getId());
            }
            rutinaRepo.deleteById(id);
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada");
        }
    }

    public List<Ejercicio> obtenerEjercicios(Long idEntrenador) {
        validarEntrenador(idEntrenador);
        return ejercicioRepo.findAllByEntrenadorId(idEntrenador);  
    }

    public Ejercicio aniadirEjercicio(Ejercicio ejercicio) {
        validarEntrenador(ejercicio.getEntrenadorId());
        ejercicio.setId(null);
        ejercicio.setEjs(Collections.emptyList());
        return ejercicioRepo.save(ejercicio);
    }

    public Ejercicio obtenerEjercicio(Long id) {
        Optional<Ejercicio> optionalEjercicio = ejercicioRepo.findById(id);
        if (optionalEjercicio.isPresent()) {
            Ejercicio ans = optionalEjercicio.get();
            validarEntrenador(ans.getEntrenadorId());
            return ans;
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrada");
        }
    }

    public void actualizarEjercicio(Ejercicio ej) {
        if (ejercicioRepo.existsById(ej.getId())) {
            validarEntrenador(ej.getEntrenadorId());
            ejercicioRepo.save(ej);
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + ej.getId() + " no fue encontrado");
        }
    }

    public void eliminarEjercicio(Long id) {
        if (ejercicioRepo.existsById(id)) {
            Ejercicio ejercicio = ejercicioRepo.getReferenceById(id);
            validarEntrenador(ejercicio.getEntrenadorId());
            if (ejercicioRepo.getReferenceById(id).getEjs().isEmpty())
                ejercicioRepo.deleteById(id);
            else
                throw new EjercicioNoEliminadoException("El ejercicio " + id + " esta siendo usado en alguna rutina");
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrado");
        }
    }

}
