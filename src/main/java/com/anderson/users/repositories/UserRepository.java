package com.anderson.users.repositories;

import com.anderson.users.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
