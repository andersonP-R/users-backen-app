package com.anderson.users.dto.mapper;

import com.anderson.users.dto.UserDto;
import com.anderson.users.models.User;

// Builder pattern
public class DtoMapperUser {

    private User user;

    // define a private constructor to avoid instance this class directly
    private DtoMapperUser() {}

    // this class only going to be available through this method so we ensure always work with same instance
    public static DtoMapperUser builder() {
        return new DtoMapperUser();
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build() {
        if (user == null) {
            throw new RuntimeException("User is null");
        }

        return new UserDto(user.getId(), user.getUsername(), user.getEmail());

    }

    // THIS PATTER LET US CHAIN MORE THAN ONE METHOD (mapper.build().setUser().build())
}
