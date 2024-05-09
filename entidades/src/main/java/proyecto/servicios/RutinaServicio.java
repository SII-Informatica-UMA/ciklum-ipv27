package proyecto.servicios;

import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

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

    public RutinaServicio(RutinaRepository rutinaRepo,
            EjercicioRepository ejercicioRepo) {
        this.rutinaRepo = rutinaRepo;
        this.ejercicioRepo = ejercicioRepo;
    }

    public List<Rutina> obtenerRutinas() {
        return rutinaRepo.findAll();
    }

    public Long aniadirRutina(Rutina rutina) { 
        if (rutinaRepo.existsByNombre(rutina.getNombre())) {
            throw new EntidadExistenteException("La rutina ya existe");
        }
        rutinaRepo.save(rutina);
        return rutina.getId();
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
        if (rutinaRepo.existsById(entidadRutina.getId())) {
            rutinaRepo.save(entidadRutina);
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + entidadRutina.getId() + " no fue encontrada");
        }
    }

    public void eliminarRutina(Long id) {
        if (rutinaRepo.existsById(id)) {
            rutinaRepo.deleteById(id);
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada");
        }
    }

    public List<Ejercicio> obtenerEjercicios() {
        List<Ejercicio> ejercicios = ejercicioRepo.findAll();
        if (ejercicios.isEmpty()) {
            throw new EntidadNoEncontradaException("No se encontraron ejercicios en la base de datos");
        }
        return ejercicios;
    }

    public Long aniadirEjercicio(Ejercicio ejercicio) {
        ejercicioRepo.save(ejercicio);
        return ejercicio.getId();
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
        if (ejercicioRepo.existsById(ej.getId())) {
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
