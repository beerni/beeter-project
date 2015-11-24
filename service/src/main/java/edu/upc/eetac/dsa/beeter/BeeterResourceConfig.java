package edu.upc.eetac.dsa.beeter;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Created by bernat on 10/10/15.
 */
public class BeeterResourceConfig extends ResourceConfig{ //son los paquetes donde buscar las clases recursos,
// los proveedores y propiedades que pueden ser utilizadas dentro de los recursos.

    public BeeterResourceConfig() { //Constructor
        packages("edu.upc.eetac.dsa.beeter"); //El paquete donde se debe buscar las clases recurso
        packages("edu.upc.eetac.dsa.beeter.auth");
        register(RolesAllowedDynamicFeature.class); // para que dentro del servicio podamos discernir el procesado o no de peticiones en función del rol que tenga el usuario
        register(DeclarativeLinkingFeature.class); //implementar la propiedad HATEOAS
        packages("edu.upc.eetac.dsa.beeter.cors"); //filtro CORS
    }
}
