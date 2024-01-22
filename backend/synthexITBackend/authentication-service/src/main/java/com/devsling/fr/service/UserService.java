package com.devsling.fr.service;


import com.devsling.fr.entities.AppUser;
import com.devsling.fr.entities.AppRole;
import com.devsling.fr.dto.SignupForm;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    AppUser findUserByUsername(String userName);

    List<AppUser> getAllUser();

    void addRole(AppRole appRole);

    void addRoleToUser(String username, String roleName);
    AppUser getUserById(Long id);

    AppUser UpdateUser(AppUser appUser);

    void DeleteUserById(Long id);

    ResponseEntity<?> signup(SignupForm signUpForm);

    void saveUser(AppUser appUser);

}
