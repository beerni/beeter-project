package edu.upc.eetac.dsa.beeter;

import edu.upc.eetac.dsa.beeter.dao.StingDAO;
import edu.upc.eetac.dsa.beeter.dao.StingDAOImpl;
import edu.upc.eetac.dsa.beeter.entity.AuthToken;
import edu.upc.eetac.dsa.beeter.entity.Sting;
import edu.upc.eetac.dsa.beeter.entity.StingCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by bernat on 13/10/15.
 */
@Path("stings") // URI relativa del recurso como stings: http://[ip]:[port]/beeter/stings
public class StingResource { //clase recurso raíz StingResource
        @Context
        private SecurityContext securityContext; //Para obtener el identificador de usuario que realiza las peticiones
    @POST
    public Response createSting(@FormParam("subject") String subject, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if(subject==null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        //Crea un sting a partir del sujeto y contenido pasado como parametro. Las marcas temporales y el id del sting los genera el servicio autom.
        //el identificador del usuario que crea el sting se obtendrá a partir del token de autenticación
        StingDAO stingDAO = new StingDAOImpl();
        Sting sting = null;
        AuthToken authenticationToken = null;
        try {
            sting = stingDAO.createSting(securityContext.getUserPrincipal().getName(), subject, content); //Obtiene el user a partir del securityContext
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + sting.getId());
        return Response.created(uri).type(BeeterMediaType.BEETER_STING).entity(sting).build();
    }
    @GET
    @Produces(BeeterMediaType.BEETER_STING_COLLECTION)
    public StingCollection getStings(@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        StingCollection stingCollection = null;
        StingDAO stingDAO = new StingDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            stingCollection = stingDAO.getStings(timestamp, before);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return stingCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(BeeterMediaType.BEETER_STING)
    public Response getSting(@PathParam("id") String id, @Context Request request) { //Te devuelve un Response. Request que realiza el cliente
        // Create cache-control
        CacheControl cacheControl = new CacheControl(); //CacheControl representa una abstracción del valor de la cabecera de respuesta HTTP Cache-Control
        Sting sting = null;
        StingDAO stingDAO = new StingDAOImpl();
        try {
            sting = stingDAO.getStingById(id);
            if (sting == null)
                throw new NotFoundException("Sting with id = " + id + " doesn't exist");

            // Calculate the ETag on last modified date of user resource
            EntityTag eTag = new EntityTag(Long.toString(sting.getLastModified()));
            /*
            La clase EntityTag representa una abstracción del valor de un Entity Tag HTTP usado como el valor de la cabecera de respuesta ETag.
            El valor se obtiene del atributo lastModified que recuperamos del recurso sting almacenado en la base de datos.
             */

            // Verify if it matched with etag available in http request
            Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);
            /*
            se evalúa si el valor de la cabecera de la petición If-None-Match coincide con el EntityTag calculado.
            Si la instancia anterior es no nula resulta que ambos valores coinciden y se responde con un estado HTTP 304 Not Modified.
             */

            if (rb != null) {
                return rb.cacheControl(cacheControl).tag(eTag).build();
            }

            // If rb is null then either it is first time request; or resource is
            // modified
            // Get the updated representation and return with Etag attached to it
            rb = Response.ok(sting).cacheControl(cacheControl).tag(eTag);
            return rb.build();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
    @Path("/{id}")
    @PUT
    @Consumes(BeeterMediaType.BEETER_STING)
    @Produces(BeeterMediaType.BEETER_STING)
    public Sting updateSting(@PathParam("id") String id, Sting sting) { //Actualiza un sting pasandole id
        if(sting == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(sting.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName(); //Obtiene el id
        if(!userid.equals(sting.getUserid()))
            throw new ForbiddenException("operation not allowed");

        StingDAO stingDAO = new StingDAOImpl();
        try {
            sting = stingDAO.updateSting(id, sting.getSubject(), sting.getContent());
            if(sting == null)
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return sting;
    }
    @Path("/{id}")
    @DELETE
    public void deleteSting(@PathParam("id") String id) { //Eliminar un sting
        String userid = securityContext.getUserPrincipal().getName(); //Obtiene el nombre del user a partir del token
        StingDAO stingDAO = new StingDAOImpl();
        try {
            String ownerid = stingDAO.getStingById(id).getUserid(); //Propietario del mensaje
            if(!userid.equals(ownerid))
                throw new ForbiddenException("operation not allowed");
            if(!stingDAO.deleteSting(id))
                throw new NotFoundException("Sting with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
