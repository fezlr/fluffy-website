package fezlr.fluffy.message.exception;

public class MessageAccessDeniedException extends RuntimeException {
    public MessageAccessDeniedException(String message) {
        super(message);
    }
}
