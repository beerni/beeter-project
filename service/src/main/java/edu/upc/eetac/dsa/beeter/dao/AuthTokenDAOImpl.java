package edu.upc.eetac.dsa.beeter.dao;

import edu.upc.eetac.dsa.beeter.auth.UserInfo;
import edu.upc.eetac.dsa.beeter.entity.AuthToken;
import edu.upc.eetac.dsa.beeter.entity.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bernat on 10/10/15.
 */
public class AuthTokenDAOImpl implements AuthTokenDAO { //Metodos de la interfaz AuthTokenDAO
    @Override
    public UserInfo getUserByAuthToken(String token) throws SQLException { //Obtiene el rol de un usuario pasandole como parametro el token
        UserInfo userInfo = null;
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Database.getConnection(); //Crea la conexión con la base de datos

            stmt = connection.prepareStatement(AuthTokenDAOQuery.GET_USER_BY_TOKEN); //Utiliza las querys de TokenDAOQuery para conseguir primero el usuario
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery(); //Ejecuta la query

            if (rs.next()) {
                userInfo = new UserInfo(); //Crea objeto de userInfo y le asigna el nombre
                userInfo.setName(rs.getString("id"));
                stmt.close();
                //Una vez tiene el usuario consigue el rol de este
                stmt = connection.prepareStatement(AuthTokenDAOQuery.GET_ROLES_OF_USER);
                stmt.setString(1, userInfo.getName());
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String role = rs.getString("role");
                    userInfo.getRoles().add(Role.valueOf(role));
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return userInfo;
    }

    @Override
    public AuthToken createAuthToken(String userid) throws SQLException { //Crea el token pasandole como parametro el userid
        Connection connection = null;
        PreparedStatement stmt = null;
        String token = null;
        AuthToken authToken = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(AuthTokenDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                token = rs.getString(1);
            else
                throw new SQLException();

            stmt.close();
            stmt = connection.prepareStatement(AuthTokenDAOQuery.CREATE_TOKEN);
            stmt.setString(1, userid);
            stmt.setString(2, token);

            stmt.executeUpdate();

            authToken = new AuthToken();
            authToken.setToken(token);
            authToken.setUserid(userid);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return authToken;
    }

    @Override
    public void deleteToken(String userid) throws SQLException { //Borra un token
        Connection connection = null;
        PreparedStatement stmt = null;
        AuthToken authToken = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(AuthTokenDAOQuery.DELETE_TOKEN);
            stmt.setString(1, userid);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }
}