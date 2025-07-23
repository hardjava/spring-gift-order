package gift.exception;

public class UnauthorizedException extends RuntimeException {
    private final String realm;

    public UnauthorizedException(String message, String realm) {
        super(message);
        this.realm = realm;
    }

    public String getRealm() {
        return realm;
    }
}
