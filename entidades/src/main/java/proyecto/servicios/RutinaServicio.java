package proyecto.servicios;

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

    public RutinaServicio(RutinaRepository rutinaRepo,
            EjercicioRepository ejercicioRepo) {
        this.rutinaRepo = rutinaRepo;
        this.ejercicioRepo = ejercicioRepo;
    }

    public List<Rutina> obtenerRutinas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerRutinas'");
    }

    public Long aniadirRutina(Rutina rutina) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aniadirRutina'");
    }

    public Rutina obtenerRutina(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerRutina'");
    }

    public void actualizarRutina(Rutina entidadRutina) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarRutina'");
    }

    public void eliminarRutina(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarRutina'");
    }

    public List<Ejercicio> obtenerEjercicios() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerEjercicios'");
    }

    public Long aniadirEjercicio(Ejercicio ejercicio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aniadirEjercicio'");
    }

    public Ejercicio obtenerEjercicio(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerEjercicio'");
    }

    public void actualizarEjercicio(Ejercicio ej) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarEjercicio'");
    }

    public void eliminarEjercicio(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarEjercicio'");
    }

}
