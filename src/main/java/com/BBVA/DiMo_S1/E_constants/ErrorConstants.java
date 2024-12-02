package com.BBVA.DiMo_S1.E_constants;

public class ErrorConstants {

    public static final String USER_NO_ENCONTRADO = "ERROR! No se encontró ningún User con el ID ingresado.";

    public static final String CONTRASEÑA_INVALIDA = "ERROR! La contraseña no es válida. Debe tener entre 6 y 20 caracteres.";

    public static final String DELETE_NO_VALIDO = "ERROR! El User que deseas eliminar ya fue eliminado.";

    public static final String ACCOUNT_NO_ENCONTRADA = "ERROR! El CBU no pertenece a una Account presente en el sistema.";

    public static final String ACCOUNT_NO_ENCONTRADA_O_DADA_DE_BAJA = "ERROR! El CBU no pertenece a una cuenta presente en el sistema " +
            "o la misma fue dada de baja.";

    public static final String CBU_INVALIDO = "ERROR! El CBU ingresado no es válido.";

    public static final String LOAN_NO_SE_PUEDE_CREAR = "ERROR! Los datos ingresados no cumplen con las condiciones para poder simular el préstamo.";

    public static final String LIMITE_TRANSACCION_INVALIDO = "ERROR! El limite de transacción ingresado no es válido. " +
            "Debe ser si o si mayor o igual a 1000.";

    public static final String CUENTA_DESACTIVADA = "ERROR! Tu cuenta se encuentra desactivada temporalmente. Activala para poder ingresar!";

    public static final String DELETE_NO_VALIDO_ACCOUNT = "ERROR! La account que deseas eliminar ya fue eliminada.";

    public static final String EMAIL_INCORRECTO = "ERROR! El email ingresado ya existe!.";

    public static final String USER_VIGENTE = "ERROR! El email ingresado no corresponde a un usuario que haya sido dado de baja.";

    public static final String CREDENCIALES_INVALIDAS = "ERROR! Credenciales invalidas!";

    public static final String ERROR_CUENTA_EN_PESOS_YA_EXISTENTE = "ERROR! Ya tenes una cuenta en pesos.";

    public static final String LIMITE_DE_CUENTAS_ALCANZADO = "ERROR! Ya alcanzaste el límite máximo de cuentas.";

    public static final String OPERACION_SOLO_ADMIN = "ERROR! No tenes permiso para dar de baja a un User distinto al tuyo.";

    public static final String ERROR_SIN_CUENTAS = "ERROR! No tenes ninguna cuenta para poder realizar la transacción.";

    public static final String ERROR_AUSENCIA_CUENTA_DOLARES = "ERROR! No tenes ninguna cuenta en USD poder realizar la transacción.";

    public static final String ERROR_AUSENCIA_CUENTA_PESOS = "ERROR! No tenes ninguna cuenta en ARS para poder realizar la transacción.";

    public static final String ERROR_EN_TRANSACCION = "ERROR! No cumplís con las condiciones para poder realizar la transacción deseada.";

    public static final String ERROR_CUENTA_PESOS = "ERROR! Para poder crear un Plazo Fijo necesitas tener una cuenta en pesos disponoble.";

    public static final String ERROR_TRANSACTION_NO_ENCONTRADA = "ERROR! No se encontró la transacción buscada.";

    public static final String SALDO_NO_DISPONIBLE = "ERROR! No tenes el dinero suficiente para poder realizar la operación que deseas.";

    public static final String ACCOUNT_NO_VALIDA = "ERROR! La cuenta a la cual le deseas enviar dinero no es del mismo tipo que la tuya.";

    public static final String ERROR_CUENTA_PROPIA = "ERROR! No podes realizar una transferencia a tu propia cuenta.";

    public static final String ERROR_ID_USUARIO_NO_ENCONTRADO = "ERROR! No existe un usuario con ese ID.";

    public static final String ERROR_CREACION_PLAZO_FIJO = "ERROR! El monto minimo a invertir en el Plazo Fijo es de 1000 y los dias deben ser 30/60/90.";

    public static final String ERROR_BALANCE = "ERROR! No se puede obtener el balance porque no tenes ninguna cuenta";

    public static final String ERROR_BALANCE_NEGATIVO = "ERROR! El deposito debe ser mayor a 0";

    public static final String ERROR_BALANCE_MAYOR_A_DEPOSITO = "ERROR! El deposito no puede ser mayor al limite de transaccion";

    public static final String ERROR_NOT_ADMIN = "ERROR! No tiene el rol administrador!";

    public static final String ERROR_TRANSACTION_NOT_EXIST = "ERROR! Este usuario no tiene transacciones";

    public static final String ERROR_NO_SE_ENCONTRO_ID_TRANSACTION = "ERROR! No existe una transaction con ese ID";

    public static final String SIN_PERMISO = "ERROR! No tienes permiso para realizar esta operación.";

    public static final String TIPO_ACCOUNT_NO_ENCONTRADO = "ERROR! No existe la cuenta indicada para realizar el pago.";

}
