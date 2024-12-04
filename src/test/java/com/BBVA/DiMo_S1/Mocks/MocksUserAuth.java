package com.BBVA.DiMo_S1.Mocks;

import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.LoginUserDTO;

public class MocksUserAuth {

    public static CreateUserDTO createMockUserDTO(){
        CreateUserDTO createUserDTO = CreateUserDTO.builder().build();
        createUserDTO.setFirstName("TestFirstName");
        createUserDTO.setLastName("TestLastname");
        createUserDTO.setEmail("email@example.com");
        createUserDTO.setPassword("123456");
        return createUserDTO;
    }

    public static LoginUserDTO loginMockUserDTO(){
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setEmail("email@example.com");
        loginUserDTO.setPassword("123456");
        return loginUserDTO;
    }
}
