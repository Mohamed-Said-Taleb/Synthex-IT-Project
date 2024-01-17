package com.devsling.fr.Service;


import com.devsling.fr.Repository.RoleRepository;
import com.devsling.fr.Repository.UserRepository;
import com.devsling.fr.Security.Request.SignUpForm;
import com.devsling.fr.Utils.ErrorModel;
import com.devsling.fr.model.Role;
import com.devsling.fr.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Configuration
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService {
   private UserRepository userRepository;
    private RoleRepository roleRepository;


    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public User findUserByUsername(String username) {
        return null;
    }

    @Override
    public List<User> getAllUser() {
        return null;
    }

    @Override
    public Role addRole(Role role) {
        return null;
    }

    @Override
    public User addRoleToUser(String username, String rolename) {
        return null;
    }

    @Override
    public Boolean isValidEmailAddress(String email) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User UpdateUser(User user) {
        return null;
    }

    @Override
    public void DeleteUserById(Long id) {

    }

    @Override
    public ResponseEntity<?> signup(SignUpForm signUpForm) {
        return null;
    }
}
