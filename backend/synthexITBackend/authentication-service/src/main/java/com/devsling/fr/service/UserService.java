package com.devsling.fr.service;


import com.devsling.fr.entities.AppRole;
import com.devsling.fr.entities.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<AppUser> findUserByUsername(String userName);
    Optional<AppUser> findUserByEmail(String email);

    List<AppUser> getAllUser();

    void addRole(AppRole appRole);

    void addRoleToUser(String username, String roleName);

    AppUser UpdateUser(AppUser appUser);

    void DeleteUserById(Long id);


    void saveUser(AppUser appUser);

}
