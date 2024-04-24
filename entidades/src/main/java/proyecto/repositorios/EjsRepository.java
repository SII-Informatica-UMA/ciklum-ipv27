package proyecto.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyecto.entidades.Ejs;
import proyecto.entidades.EjsId;

@Repository
public interface EjsRepository extends JpaRepository<Ejs,EjsId> {

    List<Ejs> findAll();
    
    @SuppressWarnings("unchecked")
    Ejs save(Ejs ejs);
    
    void deleteById(EjsId id);
}
