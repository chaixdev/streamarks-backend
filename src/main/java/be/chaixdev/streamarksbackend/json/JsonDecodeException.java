package be.chaixdev.streamarksbackend.json;

public class JsonDecodeException extends RuntimeException {

    public JsonDecodeException() {
        super();
    }

    public JsonDecodeException(String message) {
        super(message);
    }

    public JsonDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonDecodeException(Throwable cause) {
        super(cause);
    }
}
