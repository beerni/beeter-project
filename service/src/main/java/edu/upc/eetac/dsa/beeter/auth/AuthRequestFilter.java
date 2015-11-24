package edu.upc.eetac.dsa.beeter.auth;

import edu.upc.eetac.dsa.beeter.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.beeter.entity.Role;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Usuari on 05/10/2015.
 */
/*
 indicarle a Jersey que esta clase que estamos implementando, es decir, el filtro, es "interesante" anotándola con @Provider.
  Además, para asegurar que el filtro de autenticación se ejecuta tan pronto como es posible hay que asignarle una prioridad AUTHENTICATION,
   cuyo valor se declara como constante en la clase Priorities, a través de la anotación @Priority.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //Por peticiones que no necesitan token
        if(Authorized.getInstance().isAuthorized(requestContext))
            return;
        final boolean secure = requestContext.getUriInfo().getAbsolutePath().getScheme().equals("https"); //true si es en https
        String token = requestContext.getHeaderString("X-Auth-Token"); //Obtiene la cabezera para saber el token
        if (token == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        try {
            final UserInfo principal = (new AuthTokenDAOImpl()).getUserByAuthToken(token); //Obtiene info relativa a la seguridad del user
            if (principal == null)
                throw new WebApplicationException("auth token doesn't exists", Response.Status.UNAUTHORIZED);
            requestContext.setSecurityContext(new SecurityContext() {
                /*
    Rechazar peticiones que no vengan con información de autenticación con error 401 - Unauthorized.
    Hacer accesible al servicio a través de un SecurityContext:
        -el identificador de usuario autenticado que realiza la petición.
        -el/los role/s que tiene asociado/s.
        -la seguridad del canal utilizado para transportar la petición.
        -el tipo de esquema de autenticación utilizado.

                 */
                @Override
                public Principal getUserPrincipal() {
                    return principal;
                }

                @Override
                public boolean isUserInRole(String s) {
                    List<Role> roles = null;
                    if (principal != null) roles = principal.getRoles();
                    return (roles.size() > 0 && roles.contains(Role.valueOf(s)));
                }

                @Override
                public boolean isSecure() {
                    return secure;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "X-Auth-Token";
                }
            });
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
