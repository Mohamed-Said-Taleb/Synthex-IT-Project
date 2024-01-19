package com.devsling.fr.service;


import com.devsling.fr.entities.Role;
import com.devsling.fr.entities.User;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.security.request.SignUpForm;
import com.devsling.fr.tools.ErrorModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService {


   private final UserRepository userRepository;
   private final  RoleRepository roleRepository;
   private final  BCryptPasswordEncoder bCryptPasswordEncoder;
   private final UserServiceHelper userServiceHelper;


    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }


    @Override
    public Role addRole(Role roleName) {
        return roleRepository.save(roleName);
    }


    @Override
    public User addRoleToUser(String userName, String roleName) {
        User usr = userRepository.findByUsername(userName);
        Role rol = roleRepository.findByRole(roleName);
        usr.getRoles().add(rol);
        return usr;
    }



    @Override
    public User getUserById(Long id) {
        return userRepository.findUserByIdUser(id);
    }

    @Override
    public User UpdateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void DeleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> signup(SignUpForm signUpForm) {
        if(userRepository.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ErrorModel("Username is used "), HttpStatus.BAD_REQUEST);
        }
        if(signUpForm.getUsername().isEmpty()){
            return new ResponseEntity<>(new ErrorModel("Username should not be empty"),HttpStatus.BAD_REQUEST);
        }
        if(signUpForm.getPassword().isEmpty()){
            return new ResponseEntity<>(new ErrorModel("Password should not be empty"),HttpStatus.BAD_REQUEST);
        }
        if (signUpForm.getPassword().length()<5){
            return new ResponseEntity<>(new ErrorModel("Short PassWord"), HttpStatus.BAD_REQUEST);

        }
        if(signUpForm.getEmail().isEmpty()){
            return new ResponseEntity<>(new ErrorModel("Email should not be empty"),HttpStatus.BAD_REQUEST);
        }
        if(!(userServiceHelper.isValidEmailAddress(signUpForm.getEmail()))){
            return new ResponseEntity<>(new ErrorModel("Invalid email"),HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ErrorModel("email is used"),HttpStatus.BAD_REQUEST);
        }
        User userBd = User.builder()
                .username(signUpForm.getUsername())
                .email(signUpForm.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpForm.getPassword()))
                .gender(signUpForm.getGender())
                .enabled(signUpForm.getEnabled())
                .roles(Collections.singletonList(roleRepository.findByRole(signUpForm.getRole_Name())))
                .build();

        userRepository.save(userBd);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }





}
