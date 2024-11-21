package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.UserService;
import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.UserDTO;
import com.BBVA.DiMo_S1.D_models.Role;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserDTO create(final CreateUserDTO createUserDTO){
        User user = new User();
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());

        Role role = roleServiceImplementation.findById(createUserDTO.getRoleId());
        user.setRole(role);
        user = userRepository.save(user);
        return new UserDTO().fromUser(user);
    }

    @Override
    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserDTO> getAll(){
        List<User> listUser = userRepository.findAll();
        return listUser.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO convertToDto(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setCreationDate(user.getCreationDate());
        userDTO.setUpdateDate(user.getUpdateDate());
        userDTO.setRole(user.getRole().getId());
        return userDTO;

    }

}
