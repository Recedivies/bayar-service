package id.ac.ui.cs.advprog.bayarservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such bill")
public class BillDoesNotExistException extends RuntimeException {
    public BillDoesNotExistException(Integer id) {
        super("Order with id " + id + " does not exist");
    }
}
