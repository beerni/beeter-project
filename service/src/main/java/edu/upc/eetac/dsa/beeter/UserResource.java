package edu.upc.eetac.dsa.beeter;

import edu.upc.eetac.dsa.beeter.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.beeter.dao.UserAlreadyExistsException;
import edu.upc.eetac.dsa.beeter.dao.UserDAO;
import edu.upc.eetac.dsa.beeter.dao.UserDAOImpl;
import edu.upc.eetac.dsa.beeter.entity.AuthToken;
import edu.upc.eetac.dsa.beeter.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by bernat on 12/10/15.
 */
@Path("users") //para declarar la URI relativa del recurso como users, es decir, que la dirección del recurso será: http://[ip]:[port]/beeter/users
public class UserResource {//el recurso usuario
    @POST //este método está designado para procesar las peticiones HTTP POST realizadas sobre la URI relativa users
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) /*
    indica que espera los parámetros pasados según el formato application/x-www-form-urlencoded
    que es el valor que tiene la constante MediaType.APPLICATION_FORM_URLENCODED.
     La petición HTTP enviará los datos del formulario según este formato y además
      deberá añadir a la petición la cabecera Content-Type con valor application/x-www-form-urlencoded. Si no envía esta cabecera la petición será rechazada.
    */
    @Produces(BeeterMediaType.BEETER_AUTH_TOKEN) //indica que la respuesta estará en formato application/vnd.dsa.beeter.auth-token+json
    public Response registerUser(@FormParam("loginid") String loginid, @FormParam("password") String password, @FormParam("email") String email,
                                 @FormParam("fullname") String fullname, @Context UriInfo uriInfo) throws URISyntaxException {
        //Los parámetros del método registerUser() anotados con @FormParam obtienen su valor al inyectar Jersey el valor del parámetro de la petición de nombre igual al pasado en la anotación
        //UriInfo Jersey inyectará este parámetro a partir del contexto y lo utilizamos para obtener la ruta absoluta al recurso y, con ello, crear la dirección del nuevo recurso
        if(loginid == null || password == null || email == null || fullname == null)
            throw new BadRequestException("all parameters are mandatory");
        UserDAO userDAO = new UserDAOImpl(); //Utiliza UserDAOImpl para  persistir los datos del nuevo usuario en la base de datos y AuthTokenDAOImpl para obtener el token de acceso.
        User user = null;
        AuthToken authenticationToken = null;
        try{
            user = userDAO.createUser(loginid, password, email, fullname);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        }catch (UserAlreadyExistsException e){
            throw new WebApplicationException("loginid already exists", Response.Status.CONFLICT);
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        return Response.created(uri).type(BeeterMediaType.BEETER_AUTH_TOKEN).entity(authenticationToken).build();
        /*
        La respuesta se crea  a partir de la clase Response de Jersey a la que sucesivamente se le va indicando:

    -el código de estado de la petición 201 - Created y con la cabecera HTTP Location igual a la dirección del nuevo recurso: created(uri).
    -el tipo de contenido de la respuesta, es decir, el valor de la cabecera Content-Type: type(BeeterMediaType.BEETER_AUTH_TOKEN)
    -la entidad de la respuesta: entity(authenticationToken)
    y, por último, se llama al método build() para que se construya la respuesta HTTP que se le envía al cliente.

         */
    }
    @Path("/{id}") //este método está designado para procesar las peticiones HTTP GET realizadas sobre la URI relativa users/{id}
    @GET
    @Produces(BeeterMediaType.BEETER_USER)
    public User getUser(@PathParam("id") String id) {
        User user = null;
        try {
            user = (new UserDAOImpl()).getUserById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if(user == null)
            throw new NotFoundException("User with id = "+id+" doesn't exist");
        return user;
    }
    @Context
    private SecurityContext securityContext; //El identificador de usuario lo obtendremos del SecurityContext que Jersey puede inyectar sobre la clase recurso raíz.
    @Path("/{id}")
    @PUT
    @Consumes(BeeterMediaType.BEETER_USER)
    @Produces(BeeterMediaType.BEETER_USER)
    public User updateUser(@PathParam("id") String id, User user) { //Actualizar un usuario (SOLO CORREO Y FULLNAME!!!! porque no admite PATCH)
        if(user == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(user.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");

        UserDAO userDAO = new UserDAOImpl();
        try {
            user = userDAO.updateProfile(userid, user.getEmail(), user.getFullname());
            if(user == null)
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return user;
    }
    @Path("/{id}")
    @DELETE
    public void deleteUser(@PathParam("id") String id){ //ELiminar un usuario
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        UserDAO userDAO = new UserDAOImpl();
        try {
            if(!userDAO.deleteUser(id))
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }



}
