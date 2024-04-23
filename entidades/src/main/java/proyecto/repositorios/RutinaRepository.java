package proyecto.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import proyecto.entidades.Rutina;

public interface RutinaRepository extends JpaRepository<Rutina, Long> {
	List<Rutina> findByNombre(String nombre);
	
	@Query("select r from Rutina r where r.nombre = :nombre")
	List<Rutina> miConsultaCompleja(@Param("nombre") String nombre);
}