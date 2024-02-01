package com.devsling.fr.repository;

import com.devsling.fr.entities.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordToken,Long> {

    ForgetPasswordToken findByToken(String token);
}
