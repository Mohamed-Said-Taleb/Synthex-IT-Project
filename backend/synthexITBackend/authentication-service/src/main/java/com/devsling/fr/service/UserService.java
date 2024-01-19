package com.devsling.fr.service;


import com.devsling.fr.entities.Role;
import com.devsling.fr.entities.User;
import com.devsling.fr.security.request.SignUpForm;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User findUserByUsername(String userName);

    List<User> getAllUser();

    Role addRole(Role role);

    User addRoleToUser(String username, String roleName);
    User getUserById(Long id);

    User UpdateUser(User user);

    void DeleteUserById(Long id);

    ResponseEntity<?> signup(SignUpForm signUpForm);


}
