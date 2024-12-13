<div id="user-content-toc">
  <ul align="center">
    <summary><h1 style="display: inline-block">Repositorio Back Squad 1 | BBVA Fullstack Wave 5</summary>
  </ul>
</div>

------------

<div id="user-content-toc">
  <ul align="left">
    <summary><h2 style="display: inline-block">Documentación:</summary>
  </ul>
</div>

<h3>1- Autenticación</h3>

------------

🟢 **POST** -- **`/auth/register`** 
  
Endpoint para darse de alta en el sistema. El endpoint permite al usuario registrarse y le brinda el token de autorizacion para poder ingresar al sistema. Además, al mismo, se le crea inicialmente una cuenta en  ARS.

**Consideraciones**:

- Los campos deben estar completos, ninguno puede ser nulo.

- El formato del mail debe ser de la siguiente manera: (A-Z)@(A-Z).com

- La contraseña debe tener entre 6 y 20 caracteres.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "firstName": "Juan",
    "lastName": "Sanchez",
    "email": "juanSanchez@example.com",
    "password": "123456"
  }

- Salida:

   ```json
  {
   "firstName": "Juan",
   "lastName": "Sanchez",
   "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVXNlciIsImp0aSI6IjMiLCJzdW"
  }

🟢 **POST** -- **`/auth/login`** 
  
Endpoint para ingresar al sistema. El endpoint permite al usuario loguearse en el sistema, validando las credenciales, y le brinda el token de autorizacion para poder ingresar.

**Consideraciones**:

- El usuario debe estar previamente registrado y las credenciales deben ser válidas.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "email": "juanSanchez@example.com",
    "password": "123456"
  }

- Salida:

   ```json
  {
   "firstName": "Juan",
   "lastName": "Sanchez",
   "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVXNlciIsImp0aSI6IjMiLCJzdW"
  }

🟢 **POST** -- **`/auth/reactivate`** 
  
Endpoint para poder reactivar a un Usuario dado de baja. El endpoint permite a un usuario darse de alta nuevamente en el sistema en caso de haberse dado de baja.

**Consideraciones**:

- El usuario ya debe estar registrado y el email ingresado debe coincidir con el presente en el sistema.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "email": "juanSanchez@example.com",
  }

- Salida:

   ```json
   "Cuenta reactivada con éxito!" 
  
------------

<h3>2- Cuentas</h3>

------------

🔵 **GET** -- **`/accounts/`** 
  
Endpoint para obtener las cuentas creadas. El endpoint permite al usuario autenticado obtener sus cuentas presentes en el sistema.

**Consideraciones**:

- El usuario autenticado debe tener cuentas presentes en el sistema.

**Formato de salida**:

- Salida:

   ```json
    [
      {
        "titular": "Juan Sanchez",
        "cbu": "0290000111000558907080",
        "currency": "ARS",
        "transactionLimit": 100000,
        "balance": 20000
      }
    ]

🟢 **POST** -- **`/accounts/`** 
  
Endpoint para crear una Cuenta. El endpoint permite al usuario autenticado crear una cuenta en ARS o USD.

**Consideraciones**:

- El tipo de cuenta debe ser ARS o USD. Si se ingresa otro tipo, se lanza excepción.

- El límite máximo de cuentas es de 2. Una en ARS y otra en USD.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "tipoDeCuenta": "ARS"
  }

- Salida:

   ```json
   {
    "cbu": "0290000111000558907080",
    "balance": 10000,
    "currency": "ARS",
    "transactionLimit": 200000,
    "creationDate": "2024-12-04T10:06:39.752Z"
  }

🟣	**PATCH** -- **`/accounts/{cbu}`** 
  
Endpoint para actualizar el límite de transacción de una Cuenta. El endpoint permite al usuario autenticado actualizar el limite de transacción de su cuenta buscandola por CBU.

**Consideraciones**:

- El límite de transacción debe ser de 1000 para arriba. De no ser así, se lanza excepción.

- El CBU debe ser válido. Si el mismo no corresponde a una cuenta del usuario, se lanza excepción.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "transactionLimit": 1000000
  }

*Junto con el CBU como párametro*

- Salida:

   ```json
   {
    "cbu": "0290000111000558907080",
    "currency": "ARS",
    "transactionLimit": 1000000,
    "balance": 20000,
    "creationDate": "2024-12-04T10:09:22.591Z",
    "updateDate": "2024-12-04T10:09:22.591Z"
  }

🔵 **GET** -- **`/accounts/balance`** 
  
Endpoint para obtener el balance de una Cuenta. El endpoint permite al usuario autenticado obtener el balance de su/sus cuenta/s junto con la lista de transacciones y de plazos fijos.

**Formato de salida**:

- Salida:

   ```json
    {
      "balanceArs": 10000,
      "balanceUsd": 2000,
      "transactions": [
       {
        "amount": 10000,
        "currencyType": "ARS",
        "type": "payment",
        "description": "Pago de servicios",
        "transactionDate": "2024-12-04T10:12:39.494Z",
        "cuenta": "0290000111000558907080",
        "titular": "Juan Sanchez",
        "cuentaDestino": null
       }
    ],
    "fixedTermDeposits": [
      {
        "amount": 10000,
        "interest": "2%",
        "creationDate": "2024-12-04T10:12:39.494Z",
        "closingDate": "2024-12-04T10:12:39.494Z",
        "settled": true
      }
    ]
  }

🔵 **GET** -- **`/accounts/admin/{idUser}`** 
  
Endpoint para obtener las cuentas creadas. El endpoint permite a un administrador obtener las cuentas creadas pertenecientes a un determinado usuario buscandolo por ID.

**Consideraciones**:

- El usuario autenticado debe ser administrador.

- El usuario buscado debe existir en el sistema.

- El usuario buscado debe tener cuentas presentes en el sistema.

**Formato de entrada y salida**:

- Entrada: *El ID del usuario como parámetro*

**Formato de salida**:

- Salida:

   ```json
   [
    {
      "titular": "Juan Sanchez",
      "cbu": "0290000111000558907080",
      "currency": "ARS",
      "transactionLimit": 100000,
      "balance": 20000
    }
  ]

🔵 **GET** -- **`/accounts/admin/`** 
  
Endpoint para obtener las cuentas creadas de los usuarios del sistema. El endpoint permite a un administrador obtener las cuentas presentes en el sistema.

**Consideraciones**:

- El paginado de cuentas esta disponible solamente para usuarios administradores. En caso de que se desee realizar la operación como usuario sin permisos de administrador, se lanzará excepción.

**Formato de entrada y salida**:

- Entrada: *El número de pagina y el tamaño de la misma.*

**Formato de salida**:

- Salida:

   ```json
   {
  "content": [
    {
      "titular": "Juan Sanchez",
      "cbu": "37017509386631339963",
      "currency": "ARS",
      "transactionLimit": 300000,
      "balance": 10000
    }
  ],
    "pageable": {
      "pageNumber": 1,
       "pageSize": 1,
       "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
     },
      "offset": 1,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 3,
    "totalPages": 3,
    "last": false,
    "size": 1,
    "number": 1,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "numberOfElements": 1,
    "first": false,
    "empty": false
  }

------------

<h3>3- Transacciones</h3>

------------

🟢 **POST** -- **`/transactions/sendUsd`** 
  
Endpoint para realizar una transacción en dólares. El endpoint permite al usuario autenticado realizar una transacción en dólares a otra cuenta presente en el sietema.

**Consideraciones**:

- El usuario debe tener una cuenta en USD.

- El monto a transferir no puede superar el balance de la cuenta ni al límite de transacción.

- El monto a transferir debe ser > 0.

- El CBU de la cuenta destino debe corresponder a una cuenta: Existente en el sistema, en USD y no puede ser el CBU de la propia cuenta del usuario.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "amount": 10000,
    "description": "Tranferencia de Dólares",
    "cbu": "37017509386631339963"
  }

- Salida:

   ```json
   {
    "amount": 1000,
    "currencyType": "USD",
    "type": "payment",
    "description": "Tranferencia de Dólares",
    "transactionDate": "2024-12-04T10:29:25.863Z",
    "cuenta": "0290000111000558907080",
    "titular": "Juan Sanchez",
    "cuentaDestino": "37017509386631339963"
  }

🟢 **POST** -- **`/transactions/sendArs`** 
  
Endpoint para realizar una transacción en pesos. El endpoint permite al usuario autenticado realizar una transacción en pesos a otra cuenta presente en el sietema.

**Consideraciones**:

- El usuario debe tener una cuenta en ARS.

- El monto a transferir no puede superar al balance de la cuenta ni al límite de transacción.

- El monto a transferir debe ser > 0.

- El CBU de la cuenta destino debe corresponder a una cuenta: Existente en el sistema, en ARS y no puede ser el CBU de la propia cuenta del usuario.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "amount": 10000,
    "description": "Tranferencia de Pesos",
    "cbu": "37017509386631339963"
  }

- Salida:

   ```json
   {
    "amount": 1000,
    "currencyType": "ARS",
    "type": "payment",
    "description": "Tranferencia de Pesos",
    "transactionDate": "2024-12-04T10:29:25.863Z",
    "cuenta": "0290000111000558907080",
    "titular": "Juan Sanchez",
    "cuentaDestino": "37017509386631339963"
  }

🟢 **POST** -- **`/transactions/payment`** 
  
Endpoint para realizar el pago de servicios. El endpoint permite al usuario autenticado realizar el pago de servicios.

**Consideraciones**:

- El usuario debe tener la cuenta especificada.

- El monto a depositar debe ser mayor a 0 y menor al limite de transaccion.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "amount": 10000,
    "description": "Pago de servicios",
    "currencyType": "ARS"
  }

- Salida:

   ```json
   {
    "amount": 1000,
    "currencyType": "ARS",
    "type": "payment",
    "description": "Pago de servicios",
    "transactionDate": "2024-12-04T10:29:25.863Z",
    "cuenta": "0290000111000558907080",
    "titular": "Juan Sanchez",
    "cuentaDestino": null
  }

🟢 **POST** -- **`/transactions/deposit`** 
  
Endpoint para realizar un depósito en una cuenta. El endpoint permite al usuario autenticado realizar un depósito en la cuenta especificada.

**Consideraciones**:

- El usuario debe tener la cuenta especificada.

- El monto a depositar debe ser mayor a 0.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "amount": 10000,
    "description": "Deposito",
    "currencyType": "ARS"
  }

- Salida:

   ```json
   {
    "amount": 1000,
    "currencyType": "ARS",
    "type": "payment",
    "description": "Deposito",
    "transactionDate": "2024-12-04T10:29:25.863Z",
    "cuenta": "0290000111000558907080",
    "titular": "Juan Sanchez",
    "cuentaDestino": null
  }

🟣	**PATCH** -- **`/transactions/{idTransaction}`** 
  
Endpoint para actualizar la descripción de una transacción. El endpoint permite al usuario autenticado actualizar la descripción de una transacción buscandola por ID.

**Consideraciones**:

- El usuario debe tener transacciones presentes.

- El ID de transacción enviado debe coincidir con una transacción perteneciente al usuario autenticado.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "description": "Deposito para comprar comida",
  }
  
*Junto con el ID de transacción como párametro*

- Salida:

   ```json
   {
    "amount": 1000,
    "currencyType": "ARS",
    "type": "payment",
    "description": "Deposito para comprar comida",
    "transactionDate": "2024-12-04T10:29:25.863Z",
    "cuenta": "0290000111000558907080",
    "titular": "Juan Sanchez",
    "cuentaDestino": null
  }

🔵 **GET** -- **`/transactions/`** 
  
Endpoint para obtener listado de transacciones. El endpoint permite al usuario autenticado obtener el listado de sus transacciones realizadas. Las mismas, pueden ser paginadas.

**Consideraciones**:

- El usuario debe tener transacciones presentes.

**Formato de entrada y salida**:

- Entrada: *El número de página correspondiente*

- Salida:

   ```json
   {
  "content": [
     {
      "amount": 1000,
      "currencyType": "ARS",
      "type": "deposit",
      "description": "Pago de servicios",
      "transactionDate": "2024-12-04T07:41:23.386247",
      "cuenta": "26403412950277864871",
      "titular": "Juan Noli",
      "cuentaDestino": null
    }
  ],
    "pageable": {
      "pageNumber": 1,
       "pageSize": 1,
       "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
     },
      "offset": 1,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 3,
    "totalPages": 3,
    "last": false,
    "size": 1,
    "number": 1,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "numberOfElements": 1,
    "first": false,
    "empty": false
  }

🔵 **GET** -- **`/transactions/detail/{idTransaction}`** 
  
Endpoint para poder ver detalle de una transacción. El endpoint permite al usuario autenticado ver el detalle de una transacción buscandola por ID.

**Consideraciones**:

- El endpoint puede ser accesible tanto por un usuario con rol de USER como por un usuario con rol de ADMIN. La diferencia entre ambos va a estar en que, en el caso del adminstrador, va a poder ver todas las transacciones que busque y que estén dentro del sistema, le pertenezcan o no. Por el contrario, en el caso del usuario con rol USER, solo va a poder ver aquellas transacciones que, obviamente esten dentro del sistema y además, que le pertenezcan.

**Formato de entrada y salida**:

- Entrada: *El id de transaccion correspondiente*

- Salida:

   ```json
   {
    "amount": 1000,
    "currencyType": "ARS",
    "type": "payment",
    "description": "Deposito para comprar comida",
    "transactionDate": "2024-12-04T10:29:25.863Z",
    "cuenta": "0290000111000558907080",
    "titular": "Juan Sanchez",
    "cuentaDestino": null
  }

🔵 **GET** -- **`/transactions/admin/{idUser}`** 
  
Endpoint para obtener listado de transacciones. El endpoint permite al usuario administrador obtener el listado de transacciones realizadas pertenecientes a un determinado usuario buscado por ID.

**Consideraciones**:

- El usuario autenticado debe ser administrador.

- El usuario que se busca debe existir en el sistema y debe tener transacciones presentes.

**Formato de entrada y salida**:

- Entrada: *El ID de usuario correspondiente y el respectivo numero de página*

- Salida:

   ```json
   {
    "content": [
     {
      "amount": 1000,
      "currencyType": "ARS",
      "type": "deposit",
      "description": "Pago de servicios",
      "transactionDate": "2024-12-04T07:41:23.386247",
      "cuenta": "26403412950277864871",
      "titular": "Juan Noli",
      "cuentaDestino": null
    }
  ],
    "pageable": {
      "pageNumber": 1,
       "pageSize": 1,
       "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
     },
      "offset": 1,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 3,
    "totalPages": 3,
    "last": false,
    "size": 1,
    "number": 1,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "numberOfElements": 1,
    "first": false,
    "empty": false
  }

------------

<h3>4- Plazos Fijos</h3>

------------

🟢 **POST** -- **`/fixedTerm`** 
  
Endpoint para crear un Plazo Fijo. El endpoint permite al usuario autenticado crear un Plazo Fijo.

**Consideraciones**:

- El plazo fijo solo se puede crear mediante una cuenta en ARS.

- El plazo fijo puede ser de 30, 60 o 90 días.

- El interés del mismo es del 2%.

- Lo mínimo a invertir es de $1000.

- El monto a invertir debe ser menor al balance de la cuenta.

- Para la liquidación del mismo, deben espetrar: 1 minuto (30 días), 2 minutos (60 días) y 3 minutos (90 días).

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "amount": 1000,
    "cantidadDias": 30
  }

- Salida:

   ```json
   {
    "amount": 1000,
    "interest": "2%",
    "creationDate": "2024-12-04T10:52:55.937Z",
    "closingDate": "2024-12-04T10:53:55.937Z",
    "settled": false,
  }

🟢 **POST** -- **`/fixedTerm/simulate`** 
  
Endpoint para simular un Plazo Fijo. El endpoint permite al usuario autenticado simular un Plazo Fijo.

**Consideraciones**:

- El plazo fijo solo se puede crear mediante una cuenta en ARS.

- El plazo fijo puede ser de 30, 60 o 90 días.

- El interés del mismo es del 2%.

- Lo mínimo a invertir es de $1000.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "amount": 1000,
    "cantidadDias": 30
  }

- Salida:

   ```json
   {
    "amount": 1000,
    "interest": "2%",
    "creationDate": "2024-12-04T10:52:55.937Z",
    "closingDate": "2024-12-04T10:53:55.937Z",
    "settled": true,
    "interestEarned": 600,
    "finalAmount": 1600
  }
   
------------

<h3>5- Préstamos</h3>

------------

🟢 **POST** -- **`/loan/simulate`** 
  
Endpoint para simular un Préstamo. El endpoint permite al usuario autenticado simular un préstamo.

**Consideraciones**:

- El préstamo puede ser de 12, 24 o 36 meses.

- El interés es del 0.5%

- El monto a invertir debe estar entre $10000 y $10000000

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "amount": 1000,
    "plazo": 12
  }

- Salida:

   ```json
   {
    "monto": 10000,
    "plazo": 12,
    "interes": "5%",
    "cuotaMensual": 1333.33,
    "total": 16000
  }

------------

<h3>6- Usuarios</h3>

------------

🟢	**POST** -- **`/users/favList/{idUser}`** 
  
Endpoint para agregar a un usuario a la lista de usuarios favoritos. El endpoint permite al usuario autenticado agregar a un usuario a su lista de usuarios favoritos buscandolo por ID.

**Consideraciones**:

- El usuario que se desee agregar en la lista debe estar presente en el sistema.

**Formato de entrada y salida**:

- Entrada: *El ID de usuario que se desea agregar en la lista*

- Salida:

   ```json
  {
   "firstName": "Juan",
   "lastName": "Sanchez",
   "email": "juanSanchez@gmail.com",
   "creationDate": "2024-12-11T12:53:00.905Z"
  }

🟣	**PATCH** -- **`/users/update`** 
  
Endpoint para actualizar los datos del perfil. El endpoint permite al usuario autenticado actualizar los datos de su perfil.

**Consideraciones**:

- Los unicos datos que puede modificar el usuario es el nombre, apellido y contraseña.

- La contraseña debe tener entre 6 y 20 caracteres.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "firstName": "Juan",
    "lastName": "Sanchez",
    "password": "12345678910"
  }

- Salida:

   ```json
  {
    "firstName": "Juan",
    "lastName": "Sanchez",
    "password": "12345678910"
  }

🟣	**PATCH** -- **`/users/update/{idUser}`** 
  
Endpoint para actualizar los datos del perfil. El endpoint permite al usuario autenticado con rol de administrador actualizar los datos del perfil de un determinado usuario buscandolo por ID.

**Consideraciones**:

- El usuario autenticado debe ser administrador.

- El ID debe corresponder a un usuario presente en el sistema.

- Los unicos datos que puede modificar el usuario es el nombre, apellido y contraseña.

- La contraseña debe tener entre 6 y 20 caracteres.

**Formato de entrada y salida**:

- Entrada:

  ```json
  {
    "firstName": "Juan",
    "lastName": "Sanchez",
    "password": "12345678910"
  }

*Junto con el ID de usuario como párametro*

- Salida:

   ```json
  {
    "firstName": "Juan",
    "lastName": "Sanchez",
    "password": "12345678910"
  }

🔵 **GET** -- **`/users/userProfile/`** 
  
Endpoint para obtener los datos del perfil. El endpoint permite al usuario autenticado obtener los datos de su perfil.

**Consideraciones**:

- El usuario autenticado debe ser administrador.

**Formato de entrada y salida**:

- Entrada: *El numero de página y tamaño de la misma*

- Salida:

   ```json
   {
    "firstName": "Juan",
    "lastName": "Sanchez",
    "email": "juanSanchez@example.com",
    "creationDate": "2024-12-04T11:04:48.587Z"
   }

🔵 **GET** -- **`/users/favList`** 
  
Endpoint para mostrar la lista de usuarios favoritos del usuario autenticado. El endpoint permite al usuario autenticado mostrar la lista de sus usuarios favoritos.

**Formato de salida**:

- Salida:

🔵 **GET** -- **`/users/admin`** 
  
Endpoint para obtener los usuarios presentes en el sistema. El endpoint permite al usuario autenticado con rol de administrador obtener los usuarios presentes en el sistema.

**Formato de salida**:

- Salida:

   ```json
   {
    "content": [
     {
      "firstName": "Juan",
      "lastName": "Sanchez",
      "email": "juanSanchez",
      "creationDate": "2024-12-04T11:08:51.403Z",
      "updateDate": "2024-12-04T11:08:51.403Z",
      "role": "ADMIN"
    }
  ],
    "pageable": {
      "pageNumber": 1,
       "pageSize": 1,
       "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
     },
      "offset": 1,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 3,
    "totalPages": 3,
    "last": false,
    "size": 1,
    "number": 1,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "numberOfElements": 1,
    "first": false,
    "empty": false
  }

🔵 **GET** -- **`/users/admin/userProfile/{idUser}`** 
  
Endpoint para obtener los datos del perfil. El endpoint permite al usuario autenticado con rol de administrador obtener los datos del perfil de un usuario buscandolo por ID.

**Consideraciones**:

- El usuario autenticado debe ser administrador.

- El ID debe corresponder a un usuario presente en el sistema.

**Formato de entrada y salida**:

- Entrada: *El ID de usuario correspondiente*

- Salida:

   ```json
   {
    "firstName": "Juan",
    "lastName": "Sanchez",
    "email": "juanSanchez@example.com",
    "creationDate": "2024-12-04T11:04:48.587Z"
   }

🔴 **DELETE** -- **`/users`** 
  
Endpoint para darse de baja del sistema. El endpoint permite al usuario autenticado darse de baja del sistema y desactivar sus cuentas.

**Consideraciones**:

- Una vez dado de baja, el usuario no podrá iniciar sesión nuevamente

- Para poder regresar al sistema, debe activar su cuenta nuevamente.

- Sus cuentas, quedarán inhabilitadas. Las mismas no podrán recibir dinero de otra cuenta.

**Formato de salida**:

- Salida:

   ```json
   "Fuiste dado de baja con éxito!"

🔴 **DELETE** -- **`/users/admin/{idUser}`** 
  
Endpoint para dar de baja del sistema a un usuario. El endpoint permite al usuario autenticado con rol de administrador dar de baja a un usuario presente en el sistema y desactivar sus cuentas.

**Consideraciones**:

- El usuario autenticado debe ser administrador

- Si el usuario ya fue dado de baja, no se va a poder eliminarlo nuevamente

- Una vez dado de baja, el usuario no podrá iniciar sesión nuevamente

- Para poder regresar al sistema, debe activar su cuenta nuevamente.

- Sus cuentas, quedarán inhabilitadas. Las mismas no podrán recibir dinero de otra cuenta.

**Formato de entrada y salida**:

- Entrada: *El ID de usuario correspondiente*

- Salida:

   ```json
   "El usuario con ID = 3 fue dado de baja con éxito!"

------------


Pasos para ejecutar Scripts en base de datos

1) Primero iniciamos springboot y le damos run al proyecto
2) Una vez ejecutado el proyecto vamos a /main/resources/dataUser.sql y este script lo llevamos a MySql para crear nuestros roles por default y 20 users admin con 20 users normales
3) Luego de ejecutar el "dataUser.sql", ejecutar el script "dataAccount.sql" para introducir cuentas de prueba
4) A continuacion ejecutar archivo /main/resources/dataTransactions.sql para la insercion de datos de prueba de nuevas transacciones 
