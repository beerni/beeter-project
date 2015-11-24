package edu.upc.eetac.dsa.beeter.entity;

import edu.upc.eetac.dsa.beeter.*;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by bernat on 15/10/15.
 */
public class BeeterRootAPI { //Hyper links
    @InjectLinks({@InjectLink(resource = BeeterRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self bookmark home", title = "Beeter Root API")
    ,@InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "login", title = "Login",  type= BeeterMediaType.BEETER_AUTH_TOKEN)
    ,@InjectLink(resource = StingResource.class, style = InjectLink.Style.ABSOLUTE, rel = "current-stings", title = "Current stings", type= BeeterMediaType.BEETER_STING_COLLECTION),
            @InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-user", title = "Register", type= BeeterMediaType.BEETER_AUTH_TOKEN)
    ,@InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout", condition="${!empty resource.userid}")}) //Para inyectar múltiples enlaces en una List se utiliza la anotación
    /*
     enlaces a todos los recursos a los que no hace falta autorización para acceder.
    -resource: especifica una clase recurso cuya URI @Path será utilizada para construir la URI inyectada.

    -style: El estilo de la URI a inyectar. Nosotros siempre utilizaremos URI absolutas.

    -rel: Especifica la relación del link. La relación del link es un atributo descriptivo que define la relación entre el recurso fuente y el recurso destino.
    Habitualmente la relación self indica que es un enlace del recurso al propio recurso. La relación bookmark indica que este enlace es el que deben memorizar
    los clientes asegurando que siempre estará en esa URI. La relación home indica que el enlace es el punto de entrada a la aplicación.

    -title: Especifica el título, es decir, un texto legible para los humanos.

    -type: Especifica el tipo de contenido del recurso que devuelve el enlace

    -condition: Especifica una expresión booleana en Java Expression Language (EL) cuyo valor determina si el enlace se presentará en la respuesta o no.
    En este caso la condición hace que el enlace se presente si la propiedad userid de la clase recurso (resource) no es nula
     */
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
