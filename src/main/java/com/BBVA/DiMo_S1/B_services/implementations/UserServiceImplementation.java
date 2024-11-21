package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.UserService;
import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_models.Role;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleServiceImplementation roleServiceImplementation;

    //1- softDelete de un User de la BD.
    @Override
    public void softDelete(long idUser) throws CustomException {

        //Buscamos al User por ID. En caso de que no exista, lanzamos excepción.
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO));

        //En caso de que el User exista, no nos alzanza. Debemos verificar si el campo "soft_delete" == null.
        if (user.getSoftDelete() == null) {

            //Aca debería utilizar el update del propio servicio. Por ahora, a modo de prueba, alcanza.
            user.setSoftDelete(LocalDateTime.now());
            userRepository.save(user);

        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.DELETE_NO_VALIDO);
        }
    }

    @Override
    public UserDTO create(final CreateUserDTO createUserDTO) throws CustomException{

        User user = User.builder().build();

        if (userRepository.findByEmail(createUserDTO.getEmail()).isEmpty()) {
            createUserDTO.guardarDTO(user);
        } else {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.EMAIL_INCORRECTO);
        }

        Role role = roleServiceImplementation.findById(2l);
        user.setRole(role);
        user = userRepository.save(user);
        return new UserDTO().fromUser(user);
    }

    @Override
    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
