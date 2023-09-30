package chat.repositories;

public class NotSavedSubEntityException extends RuntimeException {
    private final String Message;
    public NotSavedSubEntityException(String str) {
        Message = str;
    }

    @Override
    public String toString() {
        return ("User Exception Occurred: " + Message);
    }

    @Override
    public String getMessage() {
        return ("Exception Occurred: " + Message);
    }
}
