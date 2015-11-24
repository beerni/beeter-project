package edu.upc.eetac.dsa.beeter;

/**
 * Created by bernat on 15/10/15.
 */

import edu.upc.eetac.dsa.beeter.entity.BeeterRootAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Path("/")
public class BeeterRootAPIResource { //para poder acceder vía HTTP GET a este recurso en la URI
    /*
    declararemos un atributo SecurityContext anotado con @Context para poder obtener el identificador
    del usuario que presenta el token de acceso y un atributo String cuyo valor será el identificador de usuario y
     */
    @Context
    private SecurityContext securityContext;
    private String userid;

    @GET
    @Produces(BeeterMediaType.BEETER_ROOT)
    public BeeterRootAPI getRootAPI() {
        if(securityContext.getUserPrincipal()!=null)
            userid = securityContext.getUserPrincipal().getName();
        BeeterRootAPI beeterRootAPI = new BeeterRootAPI();

        return beeterRootAPI;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
