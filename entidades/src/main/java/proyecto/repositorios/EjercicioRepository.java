package proyecto.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entidades.Ejercicio;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
    List<Ejercicio> findAll();

    @SuppressWarnings("unchecked")
    Ejercicio save(Ejercicio rutina);

    void deleteById(Long id);

}
