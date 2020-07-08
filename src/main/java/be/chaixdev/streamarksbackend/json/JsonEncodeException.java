package be.chaixdev.streamarksbackend.json;

public class JsonEncodeException extends RuntimeException {

    public JsonEncodeException() {
        super();
    }

    public JsonEncodeException(String message) {
        super(message);
    }

    public JsonEncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonEncodeException(Throwable cause) {
        super(cause);
    }
}
