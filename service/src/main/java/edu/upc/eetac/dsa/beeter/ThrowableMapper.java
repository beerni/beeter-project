package edu.upc.eetac.dsa.beeter;

import edu.upc.eetac.dsa.beeter.entity.BeeterError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by bernat on 12/10/15.
 */
@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> { //mapeador para cualquier otra excepción que no sea WebApplicationException y que interpretaremos como un error de servidor
    @Override
    public Response toResponse(Throwable throwable) {
        throwable.printStackTrace();
        BeeterError error = new BeeterError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), throwable.getMessage());
        return Response.status(error.getStatus()).entity(error).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}