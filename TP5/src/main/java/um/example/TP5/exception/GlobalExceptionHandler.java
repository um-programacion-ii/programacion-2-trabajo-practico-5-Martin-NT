package um.example.TP5.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DepartamentoNoEncontradoException.class)
    public ResponseEntity<Object> handleDepartamentoNoEncontrado(DepartamentoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DepartamentoYaExisteException.class)
    public ResponseEntity<Object> handleDepartamentoYaExiste(DepartamentoYaExisteException ex) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Object> handleEmailDuplicado(EmailDuplicadoException ex) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ResponseEntity<Object> handleEmpleadoNoEncontrado(EmpleadoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ProyectoNoEncontradoException.class)
    public ResponseEntity<Object> handleProyectoNoEncontrado(ProyectoNoEncontradoException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ProyectoYaExisteException.class)
    public ResponseEntity<Object> handleProyectoYaExiste(ProyectoYaExisteException ex) {
        return construirRespuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Metodo común para dar formato a la respuesta
    private ResponseEntity<Object> construirRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        return new ResponseEntity<>(body, status);
    }

}
