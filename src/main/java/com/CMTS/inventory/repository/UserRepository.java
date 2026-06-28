package com.CMTS.inventory.repository;

import com.CMTS.inventory.model.Production;
import com.CMTS.inventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.Role role);

    List<User> findByProduction (Production production);


}
