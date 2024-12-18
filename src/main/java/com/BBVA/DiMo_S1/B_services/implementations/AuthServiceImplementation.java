package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.AuthService;
import com.BBVA.DiMo_S1.B_services.interfaces.UserService;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.userDTO.CreateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ReactivateUserDTO;
import com.BBVA.DiMo_S1.D_dtos.userDTO.ShowCreatedUserDTO;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.Enums.CurrencyType;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AccountServiceImplementation accountServiceImplementation;

    @Autowired
    private RoleServiceImplementation roleServiceImplementation;

    //1- Registro de User en el sistema.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public ShowCreatedUserDTO createUser(final CreateUserDTO createUserDTO) throws CustomException {

        //Antes que nada, validamos el formato de la password
        //Si tiene una longitud que no esta entre 6 y 20 caracteres...
        if (createUserDTO.getPassword().length() < 6 || createUserDTO.getPassword().length() > 20) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.CONTRASEÑA_INVALIDA);
        }

        //Inicializamos al User vacío.
        User user = User.builder().build();

        //Verificamos que el mail del User que se va a registrar no exista en la BD.
        if (userRepository.findByEmail(createUserDTO.getEmail()).isEmpty()) {

            //Si no existe, guardamos los datos del DTO en el User.
            createUserDTO.guardarDTO(user);

            //Hasheo de la password
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            //Seteamos el Role del User como Usuario
            user.setRole(roleServiceImplementation.findById(2l));

            //Guardamos el User.
            user = userRepository.save(user);

            //Le creamos la cuenta en pesos.
            accountServiceImplementation.createAccount(user.getId(), CurrencyType.ARS);

        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.EMAIL_INCORRECTO);
        }

        return login(user.getEmail(), createUserDTO.getPassword());
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Login de User en el sistema.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public ShowCreatedUserDTO login(final String email, final String password) {

        ShowCreatedUserDTO showCreatedUserDTO;

        //Verificamos si las creedenciales son validas...
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email.toLowerCase(), password));

        } catch (Exception e) {

            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorConstants.CREDENCIALES_INVALIDAS);
        }

        //Si las credenciales son validas, no alcanza. Debemos validar que el User no haya sido dado de
        //baja...
        User user = userRepository.findByEmailAndSoftDeleteIsNull(email)
                .orElseThrow(() -> new CustomException(HttpStatus.CONFLICT, ErrorConstants.CUENTA_DESACTIVADA));

        //Si no fue dado de baja, cargamos el DTO.
        showCreatedUserDTO = new ShowCreatedUserDTO(user, jwtService.generateToken(user.getId(),
                email, user.getRole().getName()));

        return showCreatedUserDTO;
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Reactivación del User en el sistema.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public void reactivateUser(final ReactivateUserDTO reactivateUserDTO) {

        //Si no encuentra una cuenta que haya sido dada de baja con ese email..
        //Lanzamos excepción
        User user = userRepository.findByEmailAndSoftDeleteNotNull(reactivateUserDTO.getEmail())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_VIGENTE));

        //Si al User se lo encuentra, seteamos el softDelete en null y lo guardamos nuevamente en la BD.
        user.setSoftDelete(null);
        userRepository.save(user);
    }
    //-----------------------------------------------------------------------------------------------------------
}