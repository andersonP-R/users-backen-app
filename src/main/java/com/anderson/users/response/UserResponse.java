package com.anderson.users.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends MetadataResponse {
    UsersData response = new UsersData();
}
