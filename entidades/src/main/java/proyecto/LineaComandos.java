package proyecto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import proyecto.entidades.*;
import proyecto.repositorios.*;

@Component
public class LineaComandos implements CommandLineRunner {
	private RutinaRepository repository;

	public LineaComandos(RutinaRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		for (String s : args) {
			System.out.println(s);
		}

		if (args.length > 0) {
			for (Rutina b : repository.findAll()) {
				System.out.println(b);
			}
		}
	}

}