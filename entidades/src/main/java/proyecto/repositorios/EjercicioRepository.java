package proyecto.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entidades.Ejercicio;
import proyecto.entidades.Ejs;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
    List<Ejercicio> findAll();

    @SuppressWarnings("unchecked")
    Ejercicio save(Ejercicio ejercicio);

    boolean existsByNombre(String nombre);

    void deleteById(Long id);

    Optional<Ejs> findFirstByNombre(String nombre);

    Optional<Ejercicio> findByNombreAndIdNot(String nombre, Long id);

}
