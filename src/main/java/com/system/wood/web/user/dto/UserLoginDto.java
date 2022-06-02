package com.system.wood.web.user.dto;

import com.sun.istack.NotNull;
import lombok.Getter;

@Getter
public class UserLoginDto {

    @NotNull
    private String email;

    @NotNull
    private String password;


}
