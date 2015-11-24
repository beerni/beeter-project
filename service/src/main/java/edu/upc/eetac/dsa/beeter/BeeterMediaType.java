package edu.upc.eetac.dsa.beeter;

/**
 * Created by bernat on 12/10/15.
 */
public interface BeeterMediaType { //Los diferentes tipos de contenido (media type)
    public final static String BEETER_AUTH_TOKEN = "application/vnd.dsa.beeter.auth-token+json";
    public final static String BEETER_USER = "application/vnd.dsa.beeter.user+json";
    public final static String BEETER_STING = "application/vnd.dsa.beeter.sting+json";
    public final static String BEETER_STING_COLLECTION = "application/vnd.dsa.beeter.sting.collection+json";
    public final static String BEETER_ROOT = "application/vnd.dsa.beeter.root+json";
}
