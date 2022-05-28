package com.system.wood.web.member.dto;

import com.sun.istack.NotNull;
import lombok.Getter;

@Getter
public class MemberLoginDto {

    @NotNull
    private String email;

    @NotNull
    private String password;


}
