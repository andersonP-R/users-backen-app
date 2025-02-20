package com.anderson.users.repositories;

import com.anderson.users.models.Role;
import com.anderson.users.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
