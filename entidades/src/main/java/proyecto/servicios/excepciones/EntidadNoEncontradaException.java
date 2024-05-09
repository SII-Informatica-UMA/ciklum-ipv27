package proyecto.servicios.excepciones;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
