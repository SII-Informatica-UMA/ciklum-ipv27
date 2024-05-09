package proyecto.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entidades.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {
	List<Rutina> findAll();

	@SuppressWarnings("unchecked")
	Rutina save(Rutina rutina);
	boolean existsByNombre(String nombre);
	void deleteById(Long id);

}