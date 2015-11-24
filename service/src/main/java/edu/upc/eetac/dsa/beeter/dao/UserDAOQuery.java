package edu.upc.eetac.dsa.beeter.dao;

/**
 * Created by bernat on 10/10/15.
 */
public interface UserDAOQuery { //SQL query que necesitan los metodos
    public final static String UUID = "select REPLACE(UUID(),'-','')";//genera el identificador de usuario
    public final static String CREATE_USER = "insert into users (id, loginid, password, email, fullname) values (UNHEX(?), ?, UNHEX(MD5(?)), ?, ?);";
    public final static String UPDATE_USER = "update users set email=?, fullname=? where id=unhex(?)";
    public final static String ASSIGN_ROLE_REGISTERED = "insert into user_roles (userid, role) values (UNHEX(?), 'registered')";
    public final static String GET_USER_BY_ID = "select hex(u.id) as id, u.loginid, u.email, u.fullname from users u where id=unhex(?)";
    public final static String GET_USER_BY_USERNAME = "select hex(u.id) as id, u.loginid, u.email, u.fullname from users u where u.loginid=?";
    public final static String DELETE_USER = "delete from users where id=unhex(?)";
    public final static String GET_PASSWORD =  "select hex(password) as password from users where id=unhex(?)";
    /*
    ASPECTOS IMPORTANTES
    La función REPLACE() de MySQL elimina los guiones quedando una cadena de 32 número hexadecimales que será el identificador único de usuario.
    La función UNHEX() de MySQL convierte una cadena que representa un número en formato hexadecimal a datos binarios y la función MD5() calcula la función de hash MD5 sobre el argumento pasado como parámetro.
    La función HEX() de MySQL es la inversa de la función UNHEX().
     */
}
