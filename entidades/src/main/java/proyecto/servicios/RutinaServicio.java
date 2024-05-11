package proyecto.servicios;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.entidades.*;
import proyecto.repositorios.*;
import proyecto.servicios.excepciones.*;

@Service
@Transactional
public class RutinaServicio {

    private RutinaRepository rutinaRepo;
    private EjercicioRepository ejercicioRepo;
    private EjsRepository ejsRepo;

    public RutinaServicio(RutinaRepository rutinaRepo,
            EjercicioRepository ejercicioRepo, EjsRepository ejsRepo) {
        this.rutinaRepo = rutinaRepo;
        this.ejercicioRepo = ejercicioRepo;
        this.ejsRepo = ejsRepo;
    }

    private Optional<Ejs> refrescaEjs(Ejs ejs) {
        if (ejs.getId() != null) {
            return ejsRepo.findById(ejs.getId());
        } else {
            return Optional.empty();
        }
    }

    private void refrescarEjsRutina(Rutina rutina) {
        var ejsEnContexto = rutina.getEjercicios().stream()
                .map(ejs -> refrescaEjs(ejs)
                        .orElseThrow(() -> new EntidadNoEncontradaException("Entidad no encontrada")))
                .collect(Collectors.toList());
        rutina.setEjercicios(ejsEnContexto);
    }

    public List<Rutina> obtenerRutinas() {
        List<Rutina> rutinas = rutinaRepo.findAll();
        // if (rutinas.isEmpty()) {
        // throw new EntidadNoEncontradaException("No se encontraron rutinas en la base
        // de datos");
        // }
        return rutinas;
    }

    public Rutina aniadirRutina(Rutina rutina) {
        if (rutinaRepo.existsByNombre(rutina.getNombre())) {
            throw new EntidadExistenteException("La rutina ya existe");
        }
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
            return optionalRutina.get();
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada");
        }
    }

    public void actualizarRutina(Rutina entidadRutina) {

        Optional<Rutina> rutinaExistente = rutinaRepo.findByNombreAndIdNot(entidadRutina.getNombre(),
                entidadRutina.getId());

        if (rutinaExistente.isPresent()) {
            throw new EntidadExistenteException("La rutina nueva ya existe");
        } else if (rutinaRepo.existsById(entidadRutina.getId())) {
            List<Ejs> l = entidadRutina.getEjercicios();
            entidadRutina.setEjercicios(null);
            entidadRutina = rutinaRepo.save(entidadRutina);
            for (Ejs e : l) {
                e.setId(new EjsId(e.getEjercicio().getId(), entidadRutina.getId()));
                e.setRutina(entidadRutina);
                ejsRepo.save(e);
            }
            entidadRutina.setEjercicios(l);
            rutinaRepo.save(entidadRutina);

        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + entidadRutina.getId() + " no fue encontrada");
        }
    }

    public void eliminarRutina(Long id) {
        if (rutinaRepo.existsById(id)) {
            Rutina rutina = rutinaRepo.getReferenceById(id);
            List<Ejs> l = rutina.getEjercicios();
            for (Ejs e : l) {
                ejsRepo.deleteById(e.getId());
            }
            rutinaRepo.deleteById(id);
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada");
        }
    }

    public List<Ejercicio> obtenerEjercicios() {
        List<Ejercicio> ejercicios = ejercicioRepo.findAll();
        // if (ejercicios.isEmpty()) {
        // throw new EntidadNoEncontradaException("No se encontraron ejercicios en la
        // base de datos");
        // }
        return ejercicios;
    }

    public Ejercicio aniadirEjercicio(Ejercicio ejercicio) {
        ejercicio.setId(null);
        ejercicio.setEjs(Collections.emptyList());
        if (ejercicioRepo.existsByNombre(ejercicio.getNombre())) {
            throw new EntidadExistenteException("El ejercicio ya existe");
        }
        return ejercicioRepo.save(ejercicio);

    }

    public Ejercicio obtenerEjercicio(Long id) {
        Optional<Ejercicio> optionalEjercicio = ejercicioRepo.findById(id);
        if (optionalEjercicio.isPresent()) {
            return optionalEjercicio.get();
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrado");
        }
    }

    public void actualizarEjercicio(Ejercicio ej) {

        Optional<Ejercicio> ejercicioExistente = ejercicioRepo.findByNombreAndIdNot(ej.getNombre(), ej.getId());

        if (ejercicioExistente.isPresent()) {
            throw new EntidadExistenteException("Ya existe un ejercicio con el mismo nombre");
        } else if (ejercicioRepo.existsById(ej.getId())) {
            ejercicioRepo.save(ej);
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + ej.getId() + " no fue encontrado");
        }
    }

    public void eliminarEjercicio(Long id) {
        if (ejercicioRepo.existsById(id)) {
            ejercicioRepo.deleteById(id);
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrado");
        }
    }

}
