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
import proyecto.repositorios.EjsRepository;
import proyecto.repositorios.RutinaRepository;
import proyecto.dtos.*;

import java.net.URI;
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

	@Autowired
	private EjsRepository ejsRepository;

	@BeforeEach
	public void initializeDatabase() {
		rutinaRepository.deleteAll();
		ejercicioRepository.deleteAll();
		ejsRepository.deleteAll();
	}

	private URI uri(String scheme, String host, int port, Long entrenadorId, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		if (entrenadorId != null) {
			ub.queryParam("entrenador", entrenadorId);
		}
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, 1L, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, null, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, 1L, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, null, path);
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
		@DisplayName("devuelve la lista de rutinas vacía")
		public void devuelveRutinas() {

			var peticion = get("http", "localhost", port, "/rutina");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Rutina>>() {
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
		@DisplayName("devuelve error al obtener una rutina concreta")
		public void errorAlObtenerRutinaConcreto() {
			var peticion = get("http", "localhost", port, "/rutina/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Rutina>() {
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
		@DisplayName("devuelve error al eliminar una rutina que no existe")
		public void eliminarRutinaInexistente() {
			var peticion = delete("http", "localhost", port, "/rutina/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("inserta correctamente un ejercicio")
		public void insertaEjercicio() {

			var ejercicio = EjercicioDTO.builder()
					.nombre("Ejercicio1")
					.build();
			var peticion = post("http", "localhost", port, "/ejercicio", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:" + port + "/ejercicio");

			List<Ejercicio> ejercicioBD = ejercicioRepository.findAllByEntrenadorId(1L);
			assertThat(ejercicioBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/" + ejercicioBD.get(0).getId());
			assertThat(ejercicio.getNombre()).isEqualTo(ejercicioBD.get(0).getNombre());
		}

		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() {

			var rutina = RutinaDTO.builder()
					.nombre("Rutina1")
					.build();
			var peticion = post("http", "localhost", port, "/rutina", rutina);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:" + port + "/rutina");

			List<Rutina> rutinasBD = rutinaRepository.findAllByEntrenadorId(1L);
			assertThat(rutinasBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/" + rutinasBD.get(0).getId());
			assertThat(rutina.getNombre()).isEqualTo(rutinasBD.get(0).getNombre());
		}

	}

	@Nested
	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosLlena {

		@BeforeEach
		public void insertarDatos() {
			var ejercicio = new Ejercicio();
			ejercicio.setNombre("Ejercicio1");
			ejercicio.setEntrenadorId(1L);
			ejercicioRepository.save(ejercicio);

			var rutina = new Rutina();
			rutina.setNombre("Rutina1");
			rutina.setEntrenadorId(1L);
			rutinaRepository.save(rutina);
			var ejsId = new EjsId(ejercicio.getId(), rutina.getId());
			var ejs = new Ejs(ejsId, ejercicio, rutina, 0L, 0L, 0L);
			ejsRepository.save(ejs);
			rutina.setEjercicios(Collections.singletonList(ejs));
			rutinaRepository.save(rutina);
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
		@DisplayName("devuelve una lista de rutinas")
		public void devuelveListaRutinas() {
			var peticion = get("http", "localhost", port, "/rutina");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Rutina>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().size()).isEqualTo(1);
		}

		/*
		 * @Test
		 * 
		 * @DisplayName("da error cuando inserta un ejercicio que ya existe")
		 * public void insertaEjercicioExistente() {
		 * 
		 * var ejercicio = EjercicioDTO.builder()
		 * .nombre("Ejercicio1")
		 * .build();
		 * var peticion = post("http", "localhost", port, "/ejercicio", ejercicio);
		 * 
		 * var respuesta = restTemplate.exchange(peticion, Void.class);
		 * 
		 * assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		 * }
		 * 
		 * @Test
		 * 
		 * @DisplayName("da error cuando inserta una rutina que ya existe")
		 * public void insertaRutinaExistente() {
		 * 
		 * var rutina = RutinaDTO.builder()
		 * .nombre("Rutina1")
		 * .build();
		 * var peticion = post("http", "localhost", port, "/rutina", rutina);
		 * 
		 * var respuesta = restTemplate.exchange(peticion, Void.class);
		 * 
		 * assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		 * }
		 */

		@Test
		@DisplayName("obtiene un ejercicio concreto")
		public void obtenerEjercicioConcreto() {
			var peticion = get("http", "localhost", port, "/ejercicio/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Ejercicio>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Ejercicio1");
		}

		@Test
		@DisplayName("obtiene una rutina concreta")
		public void obtenerRutinaConcreto() {
			var peticion = get("http", "localhost", port, "/rutina/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Rutina>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Rutina1");
		}

		@Test
		@DisplayName("modificar un ejercicio correctamente")
		public void modificarEjercicio() {
			var ejercicio = EjercicioDTO.builder().nombre("EjercicioCambio").build();
			var peticion = put("http", "localhost", port, "/ejercicio/1", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepository.findById(1L).get().getNombre()).isEqualTo("EjercicioCambio");
		}

		@Test
		@DisplayName("modificar una rutina correctamente")
		public void modificarRutina() {
			var ejercicio = ejercicioRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Ejercicio not found"));

			var rutina = RutinaDTO.builder().nombre("RutinaCambio").build();
			var ejs = EjsDTO.builder().ejercicio(EjercicioDTO.fromEjercicio(ejercicio)).build();
			rutina.setEjercicios(Collections.singletonList(ejs));
			var peticion = put("http", "localhost", port, "/rutina/1", rutina);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(rutinaRepository.findById(1L).get().getNombre()).isEqualTo("RutinaCambio");
		}

		@Test
		@DisplayName("inserta correctamente una rutina con ejercicio")
		public void insertaRutina() {

			var ejercicio = ejercicioRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Ejercicio not found"));

			var rutina = RutinaDTO.builder().nombre("Rutina2").build();
			var ejs = EjsDTO.builder().ejercicio(EjercicioDTO.fromEjercicio(ejercicio)).build();
			rutina.setEjercicios(Collections.singletonList(ejs));

			var peticion = post("http", "localhost", port, "/rutina", rutina);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:" + port + "/rutina");

			List<Rutina> rutinasBD = rutinaRepository.findAllByEntrenadorId(1L);
			assertThat(rutinasBD).hasSize(2);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/" + rutinasBD.get(1).getId());
			assertThat(rutina.getNombre()).isEqualTo(rutinasBD.get(1).getNombre());
		}

		/*
		 * @Test
		 * 
		 * @DisplayName("da error al modificar un ejercicio con un nombre ya existente")
		 * public void modificarEjercicioConNombreExistente() {
		 * var cambio = new Ejercicio();
		 * cambio.setNombre("Ejercicio2");
		 * ejercicioRepository.save(cambio);
		 * var ejercicio = EjercicioDTO.builder().nombre("Ejercicio1").build();
		 * var peticion = put("http", "localhost", port, "/ejercicio/2", ejercicio);
		 * 
		 * var respuesta = restTemplate.exchange(peticion, Void.class);
		 * 
		 * assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		 * }
		 * 
		 * @Test
		 * 
		 * @DisplayName("da error al modificar una rutina con un nombre ya existente")
		 * public void modificarRutinaConNombreExistente() {
		 * var cambio = new Rutina();
		 * cambio.setNombre("Rutina2");
		 * rutinaRepository.save(cambio);
		 * var rutina = RutinaDTO.builder().nombre("Rutina1").build();
		 * var peticion = put("http", "localhost", port, "/rutina/2", rutina);
		 * 
		 * var respuesta = restTemplate.exchange(peticion, Void.class);
		 * 
		 * assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		 * }
		 */

		@Test
		@DisplayName("da error al modificar una rutina que no existe")
		public void modificarRutinaNoExistente() {
			var cambio = new Rutina();
			cambio.setNombre("Rutina2");
			var peticion = put("http", "localhost", port, "/rutina/2", cambio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("da error al eliminar un ejercicio cuando hay una rutina que lo esta usando")
		public void eliminarEjercicioConRutinaAsociada() {

			var peticion = delete("http", "localhost", port, "/ejercicio/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(417);
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

		@Test
		@DisplayName("eliminar una rutina correctamente")
		public void eliminarRutina() {
			var ejercicio = ejercicioRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Ejercicio not found"));

			var rutina = new Rutina();
			rutina.setNombre("Rutina2");
			rutinaRepository.save(rutina);
			var ejsId = new EjsId(ejercicio.getId(), rutina.getId());
			var ejs = new Ejs(ejsId, ejercicio, rutina, 0L, 0L, 0L);
			ejsRepository.save(ejs);
			rutina.setEjercicios(Collections.singletonList(ejs));
			rutinaRepository.save(rutina);

			var peticion = delete("http", "localhost", port, "/rutina/2");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(rutinaRepository.count()).isEqualTo(1);
		}

	}

}
