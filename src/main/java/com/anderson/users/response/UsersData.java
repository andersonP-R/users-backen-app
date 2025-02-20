package com.anderson.users.response;

import com.anderson.users.dto.UserDto;
import com.anderson.users.models.User;
import lombok.Data;

import java.util.List;

@Data
public class UsersData {
    List<UserDto> users;
}
