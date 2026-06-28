package db.exceptions;

import java.io.Serial;

public class DbFakeError extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1l;

    public DbFakeError(String message) {
        super(message);
    }
}
