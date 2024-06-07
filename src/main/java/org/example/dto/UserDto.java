package org.example.dto;


import lombok.Builder;
import lombok.Getter;
import org.example.entity.Role;



@Getter
@Builder
public class UserDto {
    private String name;
    private String email;
    private Role role;
    private boolean error;
    private String errorMessage;
}
