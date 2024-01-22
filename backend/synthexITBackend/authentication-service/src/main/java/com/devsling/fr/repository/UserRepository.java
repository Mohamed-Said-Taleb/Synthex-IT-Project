package com.devsling.fr.repository;


import com.devsling.fr.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    AppUser findByEmail(String email);

    AppUser findUserByIdUser(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
