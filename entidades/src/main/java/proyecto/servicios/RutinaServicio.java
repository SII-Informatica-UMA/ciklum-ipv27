package proyecto.servicios;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
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

    public List<Rutina> obtenerRutinas(Long entrenadorId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("entrenador" + entrenadorId)) {
            throw new SecurityException("No tienes permiso para acceder a estas rutinas");
        }
        List<Rutina> rutinas = rutinaRepo.findAllByEntrenadorId(entrenadorId);
        return rutinas;
    }

    public Rutina aniadirRutina(Rutina rutina) {
        /*
         * if (rutinaRepo.existsByNombre(rutina.getNombre())) {
         * throw new EntidadExistenteException("La rutina ya existe");
         * }
         */
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("entrenador" + rutina.getEntrenadorId())) {
            throw new SecurityException("No tienes permiso para agregar esta rutina");
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long entrenadorId = rutinaRepo.findById(id)
                                    .map(rutina -> rutina.getEntrenadorId())
                                    .orElseThrow(() -> new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada"));
        if (!username.equals("entrenador" + entrenadorId)) {
            throw new SecurityException("No tienes permiso para acceder a esta rutina");
        }
        Optional<Rutina> optionalRutina = rutinaRepo.findById(id);
        if (optionalRutina.isPresent()) {
            return optionalRutina.get();
        } else {
            throw new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada");
        }
    }

    public void actualizarRutina(Rutina entidadRutina) {

        /*
         * Optional<Rutina> rutinaExistente =
         * rutinaRepo.findByNombreAndIdNot(entidadRutina.getNombre(),
         * entidadRutina.getId());
         * 
         * if (rutinaExistente.isPresent()) {
         * throw new EntidadExistenteException("La rutina nueva ya existe");
         * } else
         */
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("entrenador" + entidadRutina.getEntrenadorId())) {
            throw new SecurityException("No tienes permiso para actualizar esta rutina");
        }
        if (rutinaRepo.existsById(entidadRutina.getId())) {
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long entrenadorId = rutinaRepo.findById(id)
                                        .map(rutina -> rutina.getEntrenadorId())
                                        .orElseThrow(() -> new EntidadNoEncontradaException("La rutina con ID " + id + " no fue encontrada"));
        if (!username.equals("entrenador" + entrenadorId)) {
            throw new SecurityException("No tienes permiso para eliminar esta rutina");
        }
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

    public List<Ejercicio> obtenerEjercicios(Long idEntrenador) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("entrenador" + idEntrenador)) {
            throw new SecurityException("No tienes permiso para acceder a estos ejercicios");
        }
        List<Ejercicio> ejercicios = ejercicioRepo.findAllByEntrenadorId(idEntrenador);
        return ejercicios;
    }

    public Ejercicio aniadirEjercicio(Ejercicio ejercicio) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("entrenador" + ejercicio.getEntrenadorId())) {
            throw new SecurityException("No tienes permiso para agregar este ejercicio");
        }
        ejercicio.setId(null);
        ejercicio.setEjs(Collections.emptyList());
        /*
         * if (ejercicioRepo.existsByNombre(ejercicio.getNombre())) {
         * throw new EntidadExistenteException("El ejercicio ya existe");
         * }
         */
        return ejercicioRepo.save(ejercicio);

    }

    public Ejercicio obtenerEjercicio(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long entrenadorId = ejercicioRepo.findById(id)
                                         .map(ejercicio -> ejercicio.getEntrenadorId())
                                         .orElseThrow(() -> new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrado"));
        if (!username.equals("entrenador" + entrenadorId)) {
            throw new SecurityException("No tienes permiso para acceder a este ejercicio");
        }                      
        Optional<Ejercicio> optionalEjercicio = ejercicioRepo.findById(id);
        if (optionalEjercicio.isPresent()) {
            return optionalEjercicio.get();
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrado");
        }
    }

    public void actualizarEjercicio(Ejercicio ej) {

        /*
         * Optional<Ejercicio> ejercicioExistente =
         * ejercicioRepo.findByNombreAndIdNot(ej.getNombre(), ej.getId());
         * 
         * if (ejercicioExistente.isPresent()) {
         * throw new
         * EntidadExistenteException("Ya existe un ejercicio con el mismo nombre");
         * } else
         */
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("entrenador" + ej.getEntrenadorId())) {
            throw new SecurityException("No tienes permiso para actualizar este ejercicio");
        }
        if (ejercicioRepo.existsById(ej.getId())) {
            ejercicioRepo.save(ej);
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + ej.getId() + " no fue encontrado");
        }
    }

    public void eliminarEjercicio(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long entrenadorId = ejercicioRepo.findById(id)
                                         .map(ejercicio -> ejercicio.getEntrenadorId())
                                         .orElseThrow(() -> new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrado"));
        if (!username.equals("entrenador" + entrenadorId)) {
            throw new SecurityException("No tienes permiso para eliminar este ejercicio");
        }                                 
        if (ejercicioRepo.existsById(id)) {
            if (ejercicioRepo.getReferenceById(id).getEjs().isEmpty())
                ejercicioRepo.deleteById(id);
            else
                throw new EjercicioNoEliminadoException("El ejercicio esta siendo usado en alguna rutina");
        } else {
            throw new EntidadNoEncontradaException("El ejercicio con ID " + id + " no fue encontrado");
        }
    }

}
