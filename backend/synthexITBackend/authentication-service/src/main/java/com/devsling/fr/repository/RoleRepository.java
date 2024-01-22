package com.devsling.fr.repository;


import com.devsling.fr.entities.AppRole;
import com.devsling.fr.tools.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRole(String role);

    RoleName findByRole(AppRole name);

}
