package edu.upc.eetac.dsa.beeter.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Usuari on 05/10/2015.
 */
public class Authorized {
    private static Authorized instance;
    private List<AuthorizedResource> authorizedResourcesList;

    private Authorized() throws IOException {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("authorized.json"); // se obtiene el fichero authorized.json y lo convertimos a una lista de instancias AuthorizedResource utilizando Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        authorizedResourcesList = objectMapper.readValue(in, typeFactory.constructCollectionType(List.class, AuthorizedResource.class));
    }

    public static Authorized getInstance() throws IOException {
        if (instance == null)
            instance = new Authorized();
        return instance;
    }

    public boolean isAuthorized(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();
        if(method.equals("OPTIONS"))
            return true;
        if(path.isEmpty() && method.equals("GET") && requestContext.getHeaderString("X-Auth-Token") == null)
            return true;
        for(AuthorizedResource r : authorizedResourcesList){
            if(r.getPattern().matcher(path).matches() && r.getMethods().contains(method) )
                return true;
        }
        return false;
    }
}

