package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.UserService;
import com.BBVA.DiMo_S1.C_repositories.AccountRepository;
import com.BBVA.DiMo_S1.C_repositories.UserRepository;
import com.BBVA.DiMo_S1.D_dtos.userDTO.*;
import com.BBVA.DiMo_S1.D_models.Account;
import com.BBVA.DiMo_S1.D_models.User;
import com.BBVA.DiMo_S1.E_config.JwtService;
import com.BBVA.DiMo_S1.E_constants.ErrorConstants;
import com.BBVA.DiMo_S1.E_exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    AccountServiceImplementation accountServiceImplementation;

    //1- Agregar a un usuario a la lista de usuarios favoritos.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public FavUserDTO addUserToFavList(HttpServletRequest request, String email) {

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Buscamos al usuario por mail. Si no existe, lanzamos excepción.
        User userBuscado = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USUARIO_NO_ENCONTRADO));

        //Si existe...
        User userAutenticado = userRepository.getById(userSecurityDTO.getId());

        //Verificamos que el usuario que se desea agregar no haya sido ya agregado a la lista de favoritos.
        for (User userExistente : userAutenticado.getFavoritos()) {

            if (userExistente.getId() == userBuscado.getId()) {

                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_USER_YA_AGREGADO);
            }
        }

        //Verificamos tambien que el User autenticado no se pueda agregar a si mismo a la lista de favoritos.
        if (userBuscado.getId() != userAutenticado.getId()) {

            userAutenticado.getFavoritos().add(userBuscado);
            userRepository.save(userBuscado);

        } else {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_USER_PROPIO_A_FAVORITOS);
        }

        //Antes de terminar, buscamos las cuentas del usuario.
        List<Account> cuentas = accountRepository.getByIdUser(userBuscado.getId());

        return new FavUserDTO(userBuscado, cuentas);
    }
    //-----------------------------------------------------------------------------------------------------------

    //2- Actualizar datos de perfil como usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public UpdateUserDTO updateUser(HttpServletRequest request, UpdateUserDTO updateUserDTO) {

        //Antes que nada, validamos el formato de la password
        //Si tiene una longitud que no esta entre 6 y 20 caracteres...
        if (updateUserDTO.getPassword().length() < 6 || updateUserDTO.getPassword().length() > 20) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.CONTRASEÑA_INVALIDA);
        }

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Buscamos al usuario.
        Optional<User> userOptional = userRepository.findById(userSecurityDTO.getId());
        if (userOptional.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO);
        }
        User user = userOptional.get();
        //Actualizamos sus datos.
        userOptional.get().setFirstName(updateUserDTO.getFirstName());
        userOptional.get().setLastName(updateUserDTO.getLastName());
        String passHash = BCrypt.hashpw(updateUserDTO.getPassword(), BCrypt.gensalt());
        userOptional.get().setPassword(passHash);
        userRepository.save(userOptional.get());

        return updateUserDTO;
    }
    //-----------------------------------------------------------------------------------------------------------

    //3- Actualizar datos de perfil como usuario administrador.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public UpdateUserDTO updateUserAdmin(HttpServletRequest request, Long idUser, UpdateUserDTO updateUserDTO) {

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Validamos que el usuario autenticado sea administrador.
        if (!userSecurityDTO.getRole().toUpperCase().equals("ADMIN")) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }

        //Buscamos al usuario que se quiere actualizar y lanzamos excepción si no existe.
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO));

        //Actualizamos sus datos.
        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());
        String passHash = BCrypt.hashpw(updateUserDTO.getPassword(), BCrypt.gensalt());
        user.setPassword(passHash);
        userRepository.save(user);

        return updateUserDTO;
    }
    //-----------------------------------------------------------------------------------------------------------

    //4- Obtener datos de perfil como usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public UserDTO userDetail(HttpServletRequest request) {

        //Obtenemos el usuario autenticado.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        Optional<User> user = userRepository.findById(userSecurityDTO.getId());

        return new UserDTO(user.get());
    }
    //-----------------------------------------------------------------------------------------------------------

    //5- Mostrar lista de favoritos del usuario autenticado.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public Page<FavUserDTO> showFavList(HttpServletRequest request, Pageable pageable) {

        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        Page<User> favUsersPage = userRepository.findFavUsers(userSecurityDTO.getId(), pageable);

        List<FavUserDTO> dtoList = favUsersPage.stream()
                .map(user -> {
                    // Obtener las cuentas del usuario actual
                    List<Account> cuentas = accountRepository.getByIdUser(user.getId());
                    // Crear el DTO combinando el usuario y sus cuentas
                    return new FavUserDTO(user, cuentas);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, favUsersPage.getTotalElements());
    }
    //-----------------------------------------------------------------------------------------------------------

    //6- Listar los usuarios presentes en el sistema.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public Page<FullUserDto> getAll(Pageable pageable, HttpServletRequest request) {

        //Obtenemos el usuario autenticado y el rol.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Validamos que el usuario autenticado sea administrador.
        if (!userSecurityDTO.getRole().toUpperCase().equals("ADMIN")) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }

        return userRepository.findAll(pageable)
                .map(FullUserDto::new);
    }
    //-----------------------------------------------------------------------------------------------------------

    //7- Obtener datos de perfil de un determinado usuario como administrador.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public UserDTO userDetailAdmin(HttpServletRequest request, Long idUser) {

        //Obtenemos el usuario autenticado y el rol.
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Validamos que el usuario autenticado sea administrador.
        if (!userSecurityDTO.getRole().toUpperCase().equals("ADMIN")) {
            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }

        //Buscamos al usuario que se quiere actualizar y lanzamos excepción si no existe.
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO));

        return new UserDTO((user));
    }
    //-----------------------------------------------------------------------------------------------------------

    //8- Baja de un usuario del sistema.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public void softDeleteByUser(HttpServletRequest request) throws CustomException {

        //Obtenemos el usuario autenticado
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Buscamos al User por ID. En caso de que no exista, lanzamos excepción.
        Optional<User> user = userRepository.findById(userSecurityDTO.getId());

        //Damos de baja al usuario.
        user.get().setSoftDelete(LocalDateTime.now());

        //Damos de baja sus cuentas.
        List<Account> accountList = accountRepository.getByIdUser(userSecurityDTO.getId());
        for (Account account : accountList) {
            account.setSoftDelete(LocalDateTime.now());
            accountRepository.save(account);
        }

        //Guardamos nuevamente al usuario.
        userRepository.save(user.get());
    }
    //-----------------------------------------------------------------------------------------------------------

    //9- Dar de baja del sistema a un usuario con rol de administrador.
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public void softDeleteByAdmin(HttpServletRequest request, Long idUser) throws CustomException {

        //Obtenemos el usuario autenticado
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        //Verificamos si el usuario es ADMIN
        if (!userSecurityDTO.getRole().toUpperCase().equals("ADMIN")) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_NOT_ADMIN);
        }

        //Buscamos al User por ID. Debe no haber sido eliminado de la BD.
        User user = userRepository.findByIdAndSoftDeleteIsNull(userSecurityDTO.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USER_NO_ENCONTRADO_O_ELIMINADO));

        //Damos de baja al usuario.
        user.setSoftDelete(LocalDateTime.now());

        //Damos de baja sus cuentas.
        List<Account> accountList = accountRepository.getByIdUser(userSecurityDTO.getId());
        for (Account account : accountList) {
            account.setSoftDelete(LocalDateTime.now());
            accountRepository.save(account);
        }

        //Guardamos nuevamente al usuario.
        userRepository.save(user);
    }
    //-----------------------------------------------------------------------------------------------------------

    //Eliminar a un usuario del listado de favoritos
    @Override
    @Transactional
    public void deleteFavUser(HttpServletRequest request, Long idUser) {

        //Obtenemos el usuario autenticado
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));

        userRepository.deleteUserFromFavList(userSecurityDTO.getId(), idUser);
    }


    //Obtener a un Usuario por email
    //-----------------------------------------------------------------------------------------------------------
    @Override
    public FavUserDTO getUserByEmail(HttpServletRequest request, String email) {

        //Obtenemos el usuario autenticado
        UserSecurityDTO userSecurityDTO = jwtService.validateAndGetSecurity(jwtService.extractToken(request));
        User userAutenticado = userRepository.getById(userSecurityDTO.getId());

        //Buscamos al usuario por email.
        User userBuscado = userRepository.findByEmailAndSoftDeleteIsNull(email)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorConstants.USUARIO_NO_ENCONTRADO));

        //Verificamos que el usuario que se desea agregar no haya sido ya agregado a la lista de favoritos.
        for (User userExistente : userAutenticado.getFavoritos()) {

            if (userExistente.getId() == userBuscado.getId()) {

                throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_USER_YA_AGREGADO);
            }
        }

        //Verificamos tambien que el User autenticado no se pueda agregar a si mismo a la lista de favoritos.
        if (userBuscado.getId() == userAutenticado.getId()) {

            throw new CustomException(HttpStatus.CONFLICT, ErrorConstants.ERROR_USER_PROPIO_A_FAVORITOS);
        }

        //Si no hay problemas, buscamos las cuentas del usuario
        List<Account> cuentas = accountRepository.getByIdUser(userBuscado.getId());

        return new FavUserDTO(userBuscado, cuentas);
    }
    //-----------------------------------------------------------------------------------------------------------
}