package edu.upc.eetac.dsa.beeter.dao;

import edu.upc.eetac.dsa.beeter.entity.Sting;
import edu.upc.eetac.dsa.beeter.entity.StingCollection;

import java.sql.*;

/**
 * Created by bernat on 10/10/15.
 */
public class StingDAOImpl implements StingDAO { //Metodos que llaman a las querys de StingDAOQuery...
    @Override
    public Sting createSting(String userid, String subject, String content) throws SQLException { //Crea un mensaje
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID); //Obtiene el id
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(StingDAOQuery.CREATE_STING); //Crea el mensaje
            stmt.setString(1, id);
            stmt.setString(2, userid);
            stmt.setString(3, subject);
            stmt.setString(4, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return getStingById(id);
    }

    @Override
    public Sting getStingById(String id) throws SQLException {
        Sting sting = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(StingDAOQuery.GET_STING_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sting = new Sting();
                sting.setId(rs.getString("id"));
                sting.setUserid(rs.getString("userid"));
                sting.setCreator(rs.getString("fullname"));
                sting.setSubject(rs.getString("subject"));
                sting.setContent(rs.getString("content"));
                sting.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                sting.setLastModified(rs.getTimestamp("last_modified").getTime());
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return sting;
    }

    @Override
    public StingCollection getStings(long timestamp, boolean before) throws SQLException {
        StingCollection stingCollection = new StingCollection();

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            if(before)
                stmt = connection.prepareStatement(StingDAOQuery.GET_STINGS);
            else
                stmt = connection.prepareStatement(StingDAOQuery.GET_STINGS_AFTER);
            stmt.setTimestamp(1, new Timestamp(timestamp));

            ResultSet rs = stmt.executeQuery();
            boolean first = true;
            while (rs.next()) {
                Sting sting = new Sting();
                sting.setId(rs.getString("id"));
                sting.setUserid(rs.getString("userid"));
                sting.setSubject(rs.getString("subject"));
                sting.setCreationTimestamp(rs.getTimestamp("creation_timestamp").getTime());
                sting.setLastModified(rs.getTimestamp("last_modified").getTime());
                if (first) {
                    stingCollection.setNewestTimestamp(sting.getLastModified());
                    first = false;
                }
                stingCollection.setOldestTimestamp(sting.getLastModified());
                stingCollection.getStings().add(sting);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return stingCollection;
    }

    @Override
    public Sting updateSting(String id, String subject, String content) throws SQLException {
        Sting sting = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(StingDAOQuery.UPDATE_STING);
            stmt.setString(1, subject);
            stmt.setString(2, content);
            stmt.setString(3, id);

            int rows = stmt.executeUpdate();
            if (rows == 1)
                sting = getStingById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return sting;
    }

    @Override
    public boolean deleteSting(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(StingDAOQuery.DELETE_STING);
            stmt.setString(1, id);

            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }

}
