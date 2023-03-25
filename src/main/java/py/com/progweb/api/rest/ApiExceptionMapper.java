package py.com.progweb.api.rest;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import py.com.progweb.api.dto.ApiError;
import py.com.progweb.api.exceptions.ApiException;

/**
 * This class is used to map an ApiException to a Response.
 * In this way, we can throw an ApiException from anywhere in the code
 * and the server will return a JSON response with the error message.
 */
@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {
    @Override
    public Response toResponse(ApiException e) {
        ApiError error = new ApiError(e.getMessage());
        return Response.status(e.getStatusCode()).entity(error).build();
    }
}
