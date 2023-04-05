package id.ac.ui.cs.advprog.bayarservice.exceptions.advice;

import id.ac.ui.cs.advprog.bayarservice.exceptions.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exceptions.ErrorTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {BillDoesNotExistException.class})
    public ResponseEntity<Object> orderAndMedicineNotAvailable(Exception exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

}
