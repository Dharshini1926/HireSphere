
package com.hiresphere.repository;

import com.hiresphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import com.hiresphere.entity.Role;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    long countByRole(Role role);
}
