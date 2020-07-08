package be.chaixdev.streamarksbackend.rest.common;

import java.util.List;

public abstract class Validator<T> {

    public abstract List<ValidationError> getValidationErrors(T toValidate);
}
