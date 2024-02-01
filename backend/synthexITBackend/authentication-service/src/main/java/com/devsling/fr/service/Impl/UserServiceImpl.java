package com.devsling.fr.service.Impl;


import com.devsling.fr.entities.AppRole;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final  RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public void saveUser(AppUser appUser) {
        userRepository.save(appUser);
    }
}
