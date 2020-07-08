package be.chaixdev.streamarksbackend.rest.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ApiResponse {


    public static ResponseEntity notFound(ValidationError error) {
        return errorResponseEntity(HttpStatus.NOT_FOUND, Collections.singletonList(error));
    }

    public static ResponseEntity forbidden(ValidationError error) {
        return errorResponseEntity(HttpStatus.FORBIDDEN, Collections.singletonList(error));
    }

    public static ResponseEntity errorResponseEntity(HttpStatus status, List<ValidationError> errors) {
        return ResponseEntity
                .status(status)
                .body(getErrorResponseBody(errors));

    }

    private static Map getErrorResponseBody(ValidationError error) {
        return getErrorResponseBody(Collections.singletonList(error));
    }

    private  static Map getErrorResponseBody(List<ValidationError> errors) {
        return Collections.singletonMap("errors", errors);
    }

}
