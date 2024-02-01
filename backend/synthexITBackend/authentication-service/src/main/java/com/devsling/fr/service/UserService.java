package com.devsling.fr.service;


import com.devsling.fr.entities.AppRole;
import com.devsling.fr.entities.AppUser;

import java.util.List;

public interface UserService {
    AppUser findUserByUsername(String userName);
    AppUser findUserByEmail(String email);

    List<AppUser> getAllUser();

    void addRole(AppRole appRole);

    void addRoleToUser(String username, String roleName);
    AppUser getUserById(Long id);

    AppUser UpdateUser(AppUser appUser);

    void DeleteUserById(Long id);


    void saveUser(AppUser appUser);

}
