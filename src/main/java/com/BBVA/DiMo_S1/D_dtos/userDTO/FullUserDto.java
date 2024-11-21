package com.BBVA.DiMo_S1.D_dtos.userDTO;


import lombok.Data;

@Data
public class FullUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String creationDate;
    private String updateDate;
    private Long roleId;


}
