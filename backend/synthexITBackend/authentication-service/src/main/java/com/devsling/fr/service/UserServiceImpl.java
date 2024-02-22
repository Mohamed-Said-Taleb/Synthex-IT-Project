package com.devsling.fr.service;


import com.devsling.fr.entities.AppRole;
import com.devsling.fr.entities.AppUser;
import com.devsling.fr.repository.RoleRepository;
import com.devsling.fr.repository.UserRepository;
import com.devsling.fr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class  UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final  RoleRepository roleRepository;


    @Override
    public Optional<AppUser> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<AppUser> findUserByEmail(String email) {
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
        Optional<AppUser> usr = userRepository.findByUsername(userName);
        AppRole rol = roleRepository.findByRole(roleName);
        usr.ifPresent(appUser -> appUser.getAppRoles().add(rol));
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
