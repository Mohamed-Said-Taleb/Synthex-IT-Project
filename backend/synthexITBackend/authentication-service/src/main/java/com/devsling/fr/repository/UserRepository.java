package com.devsling.fr.repository;


import com.devsling.fr.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findUserByIdUser(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
