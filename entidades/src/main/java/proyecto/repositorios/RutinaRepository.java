package proyecto.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.entidades.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {
	List<Rutina> findAll();

	Rutina save(Rutina rutina);

	void deleteById(Long id);

}