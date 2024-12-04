package com.BBVA.DiMo_S1.Mocks;

import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;

public class MocksUserAuth {
    /*    private String firstName;
    private String lastName;
    private String email;
    private String password;*/

    public static CreateUserDTO createMockUserDTO(){
        CreateUserDTO createUserDTO = CreateUserDTO.builder().build();
        createUserDTO.setFirstName("TestFirstName");
        createUserDTO.setLastName("TestLastname");
        createUserDTO.setEmail("email@example.com");
        createUserDTO.setPassword("123456");
        return createUserDTO;
    }
}
