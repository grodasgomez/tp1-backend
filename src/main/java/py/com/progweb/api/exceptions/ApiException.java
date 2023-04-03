package py.com.progweb.api.exceptions;

import javax.ws.rs.ext.Provider;

@Provider
public class ApiException extends Exception {
    private static final long serialVersionUID = 1L;
    private int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}