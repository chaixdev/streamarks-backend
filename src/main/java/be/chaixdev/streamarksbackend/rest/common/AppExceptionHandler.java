package be.chaixdev.streamarksbackend.rest.common;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IOException.class})
    protected ResponseEntity<Object> handleIOException(IOException ex, WebRequest request) {
        String bodyOfResponse = "A server error occurred";
        log.error("General server error", ex);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException ie, WebRequest request) {
        log.warn("{}", ie.getMessage(), ie);
        return handleExceptionInternal(ie, ie.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {JsonMappingException.class})
    protected ResponseEntity jsonException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("stacktrace", ExceptionUtils.getStackFrames(ex));

        log.warn("{}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(response);
    }
}
