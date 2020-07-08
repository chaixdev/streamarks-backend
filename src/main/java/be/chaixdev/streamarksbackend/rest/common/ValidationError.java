package be.chaixdev.streamarksbackend.rest.common;


import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ValidationError {
    private List<String> paths;
    private String message;

    public ValidationError(String field, String message) {
        this(Collections.singletonList(field), message);
    }

    public ValidationError(List<String> fields, String message) {
        this.paths = fields;
        this.message = message;
    }
}
