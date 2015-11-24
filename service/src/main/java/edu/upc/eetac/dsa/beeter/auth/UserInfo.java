package edu.upc.eetac.dsa.beeter.auth;

import edu.upc.eetac.dsa.beeter.entity.Role;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuari on 05/10/2015.
 */
public class UserInfo implements Principal { //autorizar una petición necesitaremos un modelo de datos en el que podamos obtener el identificador de un usuario
    // y los roles que tiene asignado
    private String name;
    private List<Role> roles = new ArrayList<>();

    public UserInfo() {
    }

    public UserInfo(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
