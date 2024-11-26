package com.BBVA.DiMo_S1.E_constants;

public class ErrorConstants {

    public static final String USER_NO_ENCONTRADO = "ERROR! No se encontró ningún User con el ID ingresado.";

    public static final String DELETE_NO_VALIDO = "ERROR! El User que deseas eliminar ya fue eliminado.";

    public static final String ACCOUNT_NO_ENCONTRADA = "ERROR! No se encontro ninguna Account con ese ID";

    public static final String DELETE_NO_VALIDO_ACCOUNT = "ERROR! La account que deseas eliminar ya fue eliminada.";

    public static final String ACCOUNT_YA_EXISTENTE = "ERROR! Ese ID ya fue ingresado en otra Account.";

    public static final String EMAIL_INCORRECTO = "ERROR! El email ingresado ya existe!.";

    public static final String CREDENCIALES_INVALIDAS = "ERROR! Credenciales invalidas!";

    public static final String OPERACION_NO_VALIDA = "ERROR! No se puede crear una cuenta nueva actualmente.";

    public static final String OPERACION_SOLO_ADMIN = "ERROR! No tenes permiso para dar de baja a un User distinto al tuyo.";

    public static final String ERROR_EN_TRANSACCION = "ERROR! No estas en condiciones de poder realizar la transacción deseada.";

    public static final String ACCOUNT_NO_VALIDA = "ERROR! La cuenta a la cual le deseas enviar dinero no es del mismo tipo que la tuya.";

    public static final String ERROR_CUENTA_PROPIA = "ERROR! No podes realizar una transferencia a tu propia cuenta.";

    public static final String ERROR_ID_USUARIO_NO_ENCONTRADO = "ERROR! No existe un usuario con ese ID.";

    public static final String ERROR_BALANCE = "ERROR! No se puede obtener el balance porque no tenes ninguna cuenta";

<<<<<<< HEAD
    public static final String ERROR_BALANCE_NEGATIVO = "ERROR! El deposito debe ser mayor a 0";

    public static final String ERROR_BALANCE_MAYOR_A_DEPOSITO = "ERROR! El deposito no puede ser mayor al limite de transaccion";



=======
    public static final String ERROR_NOT_ADMIN = "ERROR! No tiene el rol administrador!";

    public static final String ERROR_TRANSACTION_NOT_EXIST = "ERROR! Este usuario no tiene transacciones";
>>>>>>> 0e66e82a6ffa19b45e11a1b841bdcc656e72b020
}
