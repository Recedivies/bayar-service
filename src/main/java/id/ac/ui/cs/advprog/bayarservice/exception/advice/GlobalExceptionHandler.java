package id.ac.ui.cs.advprog.bayarservice.exception.advice;

import id.ac.ui.cs.advprog.bayarservice.exception.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.InvalidPaymentMethodException;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            BillDoesNotExistException.class,
            BankDoesNotExistException.class
    })
    public ResponseEntity<Object> billNotAvailableHandler(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.NOT_FOUND, "FAILED", null)
        );
    }

    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class,
            InvalidPaymentMethodException.class,
            HttpMessageNotReadableException.class,
    })
    public ResponseEntity<Object> badRequestHandler(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.BAD_REQUEST, "FAILED", null)
        );
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> methodNotAllowed(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, "FAILED", null)
        );
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseHandler.generateResponse(new Response(
                "validation error", HttpStatus.BAD_REQUEST, "FAILED", errors)
        );
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> generalError(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "FAILED", null)
        );
    }
}
