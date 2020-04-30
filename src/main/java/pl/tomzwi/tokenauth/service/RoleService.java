package pl.tomzwi.tokenauth.service;

import pl.tomzwi.tokenauth.entity.Role;
import pl.tomzwi.tokenauth.exception.RoleAlreadyDefinedException;
import pl.tomzwi.tokenauth.exception.RoleNotFoundException;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleByName(String name) throws RoleNotFoundException;

    Role addRole(String name) throws RoleAlreadyDefinedException;

}
