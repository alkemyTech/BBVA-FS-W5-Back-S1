package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.UserService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.accountDTO.AccountDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.FullUserDto;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.UserSecurityDTO;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.Role;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleServiceImplementation roleServiceImplementation;

    @Autowired
    JwtService jwtService;

    //1- softDelete de un User de la BD.
    @Override
    public void softDelete(HttpServletRequest request, long idUser) throws CustomException {

        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        //Buscamos al User por ID. En caso de que no exista, lanzamos excepción.
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO));

        //En caso de que el User exista, no nos alzanza. Debemos verificar si el campo "soft_delete" == null.
        if (user.getSoftDelete() == null) {

            if (userSecurityDTO.getRole().equals("ADMIN")) {

                //Aca debería utilizar el update del propio servicio. Por ahora, a modo de prueba, alcanza.
                user.setSoftDelete(LocalDateTime.now());

            } else {

                if (idUser == userSecurityDTO.getId()) {

                    user.setSoftDelete(LocalDateTime.now());

                } else {

                    throw new CustomException(HttpStatus.FORBIDDEN, ErrorConstants.OPERACION_SOLO_ADMIN);
                }
            }

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
        //hasheo de password
        String passHash = BCrypt.hashpw(user.getPassword(),BCrypt.gensalt());
        user.setPassword(passHash);
        Role role = roleServiceImplementation.findById(2l);
        user.setRole(role);
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Override
    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Page<FullUserDto> getAll(Pageable pageable){
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(FullUserDto::new);
    }

    @Override
    public List<AccountDTO> listarCuentasPorUsuario(long userId) throws CustomException{


        List<Account> listaCuentas = accountRepository.getByIdUser(userId);

        if (listaCuentas.isEmpty()){
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_ID_USUARIO_NO_ENCONTRADO);
        }else {
            return listaCuentas.stream().map(
                    AccountDTO::new
            ).collect(Collectors.toList());
        }
    }

    public UserDTO userDetail(HttpServletRequest request){
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extraerToken(request));

        User user = userRepository.findById(userSecurityDTO.getId()).orElseThrow(
                ()-> new CustomException(
                        HttpStatus.CONFLICT,ErrorConstants.ERROR_ID_USUARIO_NO_ENCONTRADO));

        return new UserDTO(user);

    }
}
