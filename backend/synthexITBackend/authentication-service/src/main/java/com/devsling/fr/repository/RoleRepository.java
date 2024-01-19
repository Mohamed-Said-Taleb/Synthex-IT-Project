package com.devsling.fr.repository;


import com.devsling.fr.entities.Role;
import com.devsling.fr.authEnum.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);

    RoleName findByRole(Role name);

}
