package se.magmag.oauth.endpoint;

public class EndpointException extends RuntimeException {
    public EndpointException(String message) {
        super(message);
    }
    public EndpointException(String message, Exception e) {
        super(message, e);
    }
}