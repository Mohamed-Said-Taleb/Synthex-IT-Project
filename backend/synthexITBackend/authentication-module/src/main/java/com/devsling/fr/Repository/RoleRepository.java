package com.devsling.fr.Repository;


import com.devsling.fr.Enum.RoleName;
import com.devsling.fr.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);

    RoleName findByRole(Role name);

}
