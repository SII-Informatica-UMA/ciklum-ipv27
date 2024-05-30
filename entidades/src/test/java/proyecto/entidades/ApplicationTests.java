package proyecto.entidades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import proyecto.repositorios.EjercicioRepository;
import proyecto.repositorios.EjsRepository;
import proyecto.repositorios.RutinaRepository;

import proyecto.dtos.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de ejercicios y rutinas")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RestTemplate templateMocks;

	private MockRestServiceServer mockServer;
	private ObjectMapper mapper = new ObjectMapper();

	@Value(value = "${local.server.port}")
	private int port;

	@Value(value = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzE1OTU2ODkwLCJleHAiOjE3MjU5NTY4OTB9.fnHLue1zBs_qw86FL3XYySlmSqgE8Mr9McLx2Ycn2JJapV3QjMg0Y7LRC9f8OQadS8cp_9jV5BdqfoI_gEYECA")
	private String jwtToken;

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

	private RequestEntity<Void> get(String scheme, String host, int port, String path, String jwtToken) {
		URI uri = uri(scheme, host, port, 1L, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path, String jwtToken) {
		URI uri = uri(scheme, host, port, null, path);
		var peticion = RequestEntity.delete(uri)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object, String jwtToken) {
		URI uri = uri(scheme, host, port, 1L, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object, String jwtToken) {
		URI uri = uri(scheme, host, port, null, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
				.body(object);
		return peticion;
	}

	@Nested

	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {

		@BeforeEach
		public void init() {
			MockitoAnnotations.openMocks(this);
			mockServer = MockRestServiceServer.createServer(templateMocks);
		}

		@Test
		@DisplayName("devuelve la lista de ejercicios vacía")
		public void devuelveEjercicios() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/ejercicio", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Ejercicio>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
			mockServer.verify();
		}

		@Test
		@DisplayName("devuelve la lista de rutinas vacía")
		public void devuelveRutinas() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/rutina", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Rutina>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
			mockServer.verify();
		}

		@Test
		@DisplayName("devuelve error al obtener un ejercicio concreto")
		public void errorAlObtenerEjercicioConcreto() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/ejercicio/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Ejercicio>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

		}

		@Test
		@DisplayName("devuelve error al obtener una rutina concreta")
		public void errorAlObtenerRutinaConcreto() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/rutina/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Rutina>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al modificar un ejercicio que no existe")
		public void modificarEjercicioInexistente() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio1").build();
			var peticion = put("http", "localhost", port, "/ejercicio/1", ejercicio,
					jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar un ejercicio que no existe")
		public void eliminarEjercicioInexistente() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = delete("http", "localhost", port, "/ejercicio/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar una rutina que no existe")
		public void eliminarRutinaInexistente() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = delete("http", "localhost", port, "/rutina/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("inserta correctamente un ejercicio")
		public void insertaEjercicio() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var ejercicio = EjercicioDTO.builder()
					.nombre("Ejercicio1")
					.build();
			var peticion = post("http", "localhost", port, "/ejercicio", ejercicio,
					jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:" + port + "/ejercicio");

			List<Ejercicio> ejercicioBD = ejercicioRepository.findAllByEntrenadorId(1L);
			assertThat(ejercicioBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/" + ejercicioBD.get(0).getId());
			assertThat(ejercicio.getNombre()).isEqualTo(ejercicioBD.get(0).getNombre());
			mockServer.verify();
		}

		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var rutina = RutinaDTO.builder()
					.nombre("Rutina1")
					.build();
			var peticion = post("http", "localhost", port, "/rutina", rutina, jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:" + port + "/rutina");

			List<Rutina> rutinasBD = rutinaRepository.findAllByEntrenadorId(1L);
			assertThat(rutinasBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/" + rutinasBD.get(0).getId());
			assertThat(rutina.getNombre()).isEqualTo(rutinasBD.get(0).getNombre());
			mockServer.verify();
		}

	}

	@Nested

	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosLlena {

		@BeforeEach
		public void insertarDatos() {
			MockitoAnnotations.openMocks(this);
			mockServer = MockRestServiceServer.createServer(templateMocks);
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
		@DisplayName("devuelve error cuando un usuario no tiene acceso a los ejercicios del entrenador")
		public void noAutorizacionEjercicios() throws JsonProcessingException, URISyntaxException {
			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(2L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/ejercicio", jwtToken);

			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Ejercicio>>() {
			});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}

		@Test
		@DisplayName("devuelve error cuando un usuario no tiene acceso a las rutinas del entrenador")
		public void noAutorizacionRutinas() throws JsonProcessingException, URISyntaxException {
			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(2L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/rutina", jwtToken);

			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Ejercicio>>() {
			});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}

		@Test
		@DisplayName("devuelve una lista de ejercicios")
		public void devuelveListaEjercicios() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/ejercicio", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Ejercicio>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().size()).isEqualTo(1);
			mockServer.verify();
		}

		@Test

		@DisplayName("devuelve una lista de rutinas")
		public void devuelveListaRutinas() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/rutina", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Rutina>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().size()).isEqualTo(1);
			mockServer.verify();
		}

		@Test

		@DisplayName("obtiene un ejercicio concreto")
		public void obtenerEjercicioConcreto() throws URISyntaxException,
				JsonProcessingException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/ejercicio/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Ejercicio>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Ejercicio1");
			mockServer.verify();
		}

		@Test

		@DisplayName("obtiene una rutina concreta")
		public void obtenerRutinaConcreto() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = get("http", "localhost", port, "/rutina/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<Rutina>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Rutina1");
			mockServer.verify();
		}

		@Test

		@DisplayName("modificar un ejercicio correctamente")
		public void modificarEjercicio() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var ejercicio = EjercicioDTO.builder().nombre("EjercicioCambio").build();
			ejercicio.setEntrenadorId(1L);
			var peticion = put("http", "localhost", port, "/ejercicio/1", ejercicio,
					jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepository.findById(1L).get().getNombre()).isEqualTo(
					"EjercicioCambio");
			mockServer.verify();
		}

		@Test

		@DisplayName("modificar una rutina correctamente")
		public void modificarRutina() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var ejercicio = ejercicioRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Ejercicio not found"));

			var rutina = RutinaDTO.builder().nombre("RutinaCambio").build();
			var ejs = EjsDTO.builder().ejercicio(EjercicioDTO.fromEjercicio(ejercicio)).build();
			rutina.setEjercicios(Collections.singletonList(ejs));
			rutina.setEntrenadorId(1L);
			var peticion = put("http", "localhost", port, "/rutina/1", rutina, jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(rutinaRepository.findById(1L).get().getNombre()).isEqualTo(
					"RutinaCambio");
			mockServer.verify();
		}

		@Test

		@DisplayName("inserta correctamente una rutina con ejercicio")
		public void insertaRutina() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var ejercicio = ejercicioRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Ejercicio not found"));

			var rutina = RutinaDTO.builder().nombre("Rutina2").build();
			var ejs = EjsDTO.builder().ejercicio(EjercicioDTO.fromEjercicio(ejercicio)).build();
			rutina.setEjercicios(Collections.singletonList(ejs));

			var peticion = post("http", "localhost", port, "/rutina", rutina, jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:" + port + "/rutina");

			List<Rutina> rutinasBD = rutinaRepository.findAllByEntrenadorId(1L);
			assertThat(rutinasBD).hasSize(2);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/" + rutinasBD.get(1).getId());
			assertThat(rutina.getNombre()).isEqualTo(rutinasBD.get(1).getNombre());
			mockServer.verify();
		}

		@Test

		@DisplayName("da error al modificar una rutina que no existe")
		public void modificarRutinaNoExistente() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var cambio = new Rutina();
			cambio.setNombre("Rutina2");
			var peticion = put("http", "localhost", port, "/rutina/2", cambio, jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

		}

		@Test

		@DisplayName("da error al intentar acceder como un entrenador que no existe")
		public void EntrenadorNoExistente() throws JsonProcessingException,
				URISyntaxException {

			mockServer.expect(ExpectedCount.once(),
				requestTo(new URI("http://localhost:8080/entrenador/1")))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

			var peticion = get("http", "localhost", port, "/rutina/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			mockServer.verify();
		}

		@Test

		@DisplayName("da error al eliminar un ejercicio cuando hay una rutina que lo esta usando")
		public void eliminarEjercicioConRutinaAsociada() throws JsonProcessingException, URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var peticion = delete("http", "localhost", port, "/ejercicio/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(417);
			mockServer.verify();
		}

		@Test

		@DisplayName("eliminar un ejercicio correctamente")
		public void eliminarEjercicio() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var ejercicio = new Ejercicio();
			ejercicio.setNombre("Ejercicio2");
			ejercicio.setEntrenadorId(1L);
			ejercicioRepository.save(ejercicio);

			var peticion = delete("http", "localhost", port, "/ejercicio/2", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepository.count()).isEqualTo(1);
			mockServer.verify();
		}

		@Test

		@DisplayName("eliminar una rutina correctamente")
		public void eliminarRutina() throws JsonProcessingException,
				URISyntaxException {

			EntrenadorDTO entrenadorDTO = new EntrenadorDTO();
			entrenadorDTO.setId(1L);
			entrenadorDTO.setUsuarioId(1L);

			mockServer.expect(ExpectedCount.once(),
					requestTo(new URI("http://localhost:8080/entrenador/1")))
					.andRespond(withStatus(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(mapper.writeValueAsString(entrenadorDTO)));

			var ejercicio = ejercicioRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Ejercicio not found"));

			var rutina = new Rutina();
			rutina.setNombre("Rutina2");
			rutinaRepository.save(rutina);
			var ejsId = new EjsId(ejercicio.getId(), rutina.getId());
			var ejs = new Ejs(ejsId, ejercicio, rutina, 0L, 0L, 0L);
			ejsRepository.save(ejs);
			rutina.setEjercicios(Collections.singletonList(ejs));
			rutina.setEntrenadorId(1L);
			rutinaRepository.save(rutina);

			var peticion = delete("http", "localhost", port, "/rutina/2", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(rutinaRepository.count()).isEqualTo(1);
			mockServer.verify();
		}

	}

}
