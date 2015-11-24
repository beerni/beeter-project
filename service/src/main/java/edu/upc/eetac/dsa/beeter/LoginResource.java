package edu.upc.eetac.dsa.beeter;

import edu.upc.eetac.dsa.beeter.dao.AuthTokenDAO;
import edu.upc.eetac.dsa.beeter.dao.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.beeter.dao.UserDAO;
import edu.upc.eetac.dsa.beeter.dao.UserDAOImpl;
import edu.upc.eetac.dsa.beeter.entity.AuthToken;
import edu.upc.eetac.dsa.beeter.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

/**
 * Created by bernat on 13/10/15.
 */
@Path("login")
public class LoginResource { //Recurso raíz para poder loguearse y desloguearse
    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(BeeterMediaType.BEETER_AUTH_TOKEN)
    public AuthToken login(@FormParam("login") String loginid, @FormParam("password") String password) { //Le pasa el loginid y su password
        /*
        consiste en la creación de un nuevo token de acceso al usuario que se identifica con
        su identificador de login y su contraseña
         */
        if(loginid == null || password == null)
            throw new BadRequestException("all parameters are mandatory");

        User user = null;
        AuthToken authToken = null;
        try{
            UserDAO userDAO = new UserDAOImpl(); //Crea usuarioDAO
            user = userDAO.getUserByLoginid(loginid); //Consigue el loginid
            if(user == null)
                throw new BadRequestException("loginid " + loginid + " not found.");
            if(!userDAO.checkPassword(user.getId(), password))
                throw new BadRequestException("incorrect password");

            AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl(); //Tokens
            authTokenDAO.deleteToken(user.getId());
            authToken = authTokenDAO.createAuthToken(user.getId());
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        return authToken;
    }

    @DELETE
    public void logout(){ //Logout = le quita el token
        String userid = securityContext.getUserPrincipal().getName(); //Consigue el nombre
        AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
        try {
            authTokenDAO.deleteToken(userid); //Lo elimina
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
