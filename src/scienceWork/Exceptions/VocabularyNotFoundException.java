package scienceWork.Exceptions;

public class VocabularyNotFoundException extends Exception {
    public VocabularyNotFoundException() {
    }

    public VocabularyNotFoundException(String message) {
        super(message);
    }

    public VocabularyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VocabularyNotFoundException(Throwable cause) {
        super(cause);
    }

    public VocabularyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
