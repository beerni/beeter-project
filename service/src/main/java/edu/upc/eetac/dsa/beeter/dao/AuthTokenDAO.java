package edu.upc.eetac.dsa.beeter.dao;

import edu.upc.eetac.dsa.beeter.auth.UserInfo;
import edu.upc.eetac.dsa.beeter.entity.AuthToken;

import java.sql.SQLException;

/**
 * Created by bernat on 10/10/15.
 */
public interface AuthTokenDAO { //Interfaz para los tokens
    public UserInfo getUserByAuthToken(String token) throws SQLException; //Obtiene el usuario que tiene el token
    public AuthToken createAuthToken(String userid) throws SQLException; //Crea un token para un usuario
    public void deleteToken(String userid) throws  SQLException; //borra token
}
