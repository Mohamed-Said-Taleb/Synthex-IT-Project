package com.devsling.fr.Service;


import com.devsling.fr.Security.Request.SignUpForm;
import com.devsling.fr.model.Role;
import com.devsling.fr.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User findUserByUsername(String username);

    List<User> getAllUser();

    Role addRole(Role role);

    User addRoleToUser(String username, String rolename);

    public Boolean isValidEmailAddress(String email);

    User getUserById(Long id);

    User UpdateUser(User user);

    void DeleteUserById(Long id);

    ResponseEntity<?> signup(SignUpForm signUpForm);


}
