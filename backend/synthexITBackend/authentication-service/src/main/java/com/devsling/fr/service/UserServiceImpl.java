package com.devsling.fr.service;


import com.devsling.fr.entities.AppUser;
import com.devsling.fr.entities.AppRole;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.dto.SignupForm;
import com.devsling.fr.tools.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class  UserServiceImpl implements UserService {


   private final UserRepository userRepository;
   private final  RoleRepository roleRepository;
   private final  BCryptPasswordEncoder bCryptPasswordEncoder;
   private final UserServiceHelper userServiceHelper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserServiceHelper userServiceHelper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userServiceHelper = userServiceHelper;
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getAllUser() {
        return userRepository.findAll();
    }


    @Override
    public void addRole(AppRole appRoleName) {
        roleRepository.save(appRoleName);
    }


    @Override
    public void addRoleToUser(String userName, String roleName) {
        AppUser usr = userRepository.findByUsername(userName);
        AppRole rol = roleRepository.findByRole(roleName);
        usr.getAppRoles().add(rol);
    }



    @Override
    public AppUser getUserById(Long id) {
        return userRepository.findUserByIdUser(id);
    }

    @Override
    public AppUser UpdateUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    @Override
    public void DeleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<?> signup(SignupForm signUpForm) {
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
        AppUser appUserBd = AppUser.builder()
                .username(signUpForm.getUsername())
                .email(signUpForm.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpForm.getPassword()))
                .gender(signUpForm.getGender())
                .enabled(signUpForm.getEnabled())
                .appRoles(Collections.singletonList(roleRepository.findByRole(signUpForm.getRole_Name())))
                .build();

        userRepository.save(appUserBd);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public void saveUser(AppUser appUser) {
        userRepository.save(appUser);
    }





}
