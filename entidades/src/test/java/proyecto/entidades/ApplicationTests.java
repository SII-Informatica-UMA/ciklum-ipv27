package proyecto.entidades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import proyecto.repositorios.EjercicioRepository;
import proyecto.repositorios.RutinaRepository;
import proyecto.entidades.*;
import proyecto.dtos.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de ejercicios y rutinas")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	private RutinaRepository rutinaRepository;

	@Autowired
	private EjercicioRepository ejercicioRepository;

	@BeforeEach
	public void initializeDatabase() {
		rutinaRepository.deleteAll();
		ejercicioRepository.deleteAll();
	}

	private URI uri(String scheme, String host, int port, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	@Nested
	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {

		@Test
		@DisplayName("devuelve la lista de ejercicios vacía")
		public void devuelveEjercicios() {

			var peticion = get("http", "localhost", port, "/ejercicio");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Ejercicio>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}

		@Test
		@DisplayName("devuelve error al obtener un ejercicio concreto")
		public void errorAlObtenerEjercicioConcreto() {
			var peticion = get("http", "localhost", port, "/ejercicio/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Ejercicio>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al modificar un ejercicio que no existe")
		public void modificarEjercicioInexistente() {
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio1").build();
			var peticion = put("http", "localhost", port, "/ejercicio/1", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar un ejercicio que no existe")
		public void eliminarEjercicioInexistente() {
			var peticion = delete("http", "localhost", port, "/ejercicio/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("inserta correctamente un ejercicio")
		public void insertaEjercicio() {

			// Preparamos el ingrediente a insertar
			var ejercicio = EjercicioDTO.builder()
					.nombre("Ejercicio1")
					.build();
			// Preparamos la petición con el ingrediente dentro
			var peticion = post("http", "localhost", port, "/ejercicio", ejercicio);

			// Invocamos al servicio REST
			var respuesta = restTemplate.exchange(peticion, Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:" + port + "/ejercicio");

			List<Ejercicio> ejercicioBD = ejercicioRepository.findAll();
			assertThat(ejercicioBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/" + ejercicioBD.get(0).getId());
			assertThat(ejercicio.getNombre()).isEqualTo(ejercicioBD.get(0).getNombre());
		}

	}

	@Nested
	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosLlena {

		@BeforeEach
		public void insertarDatos() {
			var ejercicio = new Ejercicio();
			ejercicio.setNombre("Ejercicio1");
			ejercicioRepository.save(ejercicio);
		}

		@Test
		@DisplayName("devuelve una lista de ejercicios")
		public void devuelveListaEjercicios() {
			var peticion = get("http", "localhost", port, "/ejercicio");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Ejercicio>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().size()).isEqualTo(1);
		}

		@Test
		@DisplayName("da error cuando inserta un ejercicio que ya existe")
		public void insertaEjercicioExistente() {

			// Preparamos el ingrediente a insertar
			var ejercicio = EjercicioDTO.builder()
					.nombre("Ejercicio1")
					.build();
			// Preparamos la petición con el ingrediente dentro
			var peticion = post("http", "localhost", port, "/ejercicio", ejercicio);

			// Invocamos al servicio REST
			var respuesta = restTemplate.exchange(peticion, Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}

		@Test
		@DisplayName("obtiene un ejercicio concreto")
		public void obtenerNivelConcreto() {
			var peticion = get("http", "localhost", port, "/ejercicio/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Ejercicio>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Ejercicio1");
		}

		@Test
		@DisplayName("modificar un ejercicio correctamente")
		public void modificarNivel() {
			var ejercicio = EjercicioDTO.builder().nombre("EjercicioCambio").build();
			var peticion = put("http", "localhost", port, "/ejercicio/1", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepository.findById(1L).get().getNombre()).isEqualTo("EjercicioCambio");
		}

		@Test
		@DisplayName("da error al modificar un ejercicio con un nombre ya existente")
		public void modificarEjercicioConNombreExistente() {
			var cambio = new Ejercicio();
			cambio.setNombre("Ejercicio2");
			ejercicioRepository.save(cambio);
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio1").build();
			var peticion = put("http", "localhost", port, "/ejercicio/2", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}

		@Test
		@DisplayName("eliminar un ejercicio correctamente")
		public void eliminarEjercicio() {
			var ejercicio = new Ejercicio();
			ejercicio.setNombre("Ejercicio2");
			ejercicioRepository.save(ejercicio);

			var peticion = delete("http", "localhost", port, "/ejercicio/2");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepository.count()).isEqualTo(1);
		}

	}

}
