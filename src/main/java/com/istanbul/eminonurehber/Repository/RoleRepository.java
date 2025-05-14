package com.istanbul.eminonurehber.Repository;

import com.istanbul.eminonurehber.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);  // Rolü isme göre bul

 //   Optional<Role> findByRoleName(String roleName); // Rol ismine göre rol bulma
}
