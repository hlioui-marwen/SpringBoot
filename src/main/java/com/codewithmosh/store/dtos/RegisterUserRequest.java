package com.codewithmosh.store.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Data : combination of getter and setter
public class RegisterUserRequest {
    private String name;
    private String password;
    private String email;
}
