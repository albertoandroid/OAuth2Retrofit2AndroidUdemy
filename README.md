# OAuth2Retrofit2AndroidUdemy

OAuth2 es un protocolo/Framework de Autorización que permite que una aplicación de terceros obtenga acceso limitado aun servicio HTTP, ya sea en nombre de un propietarios de recursos mediante la organización de una interacción de aprobación entre el propietarios del recurso y el servicio de HTTP o permitiendo que la ablación de terrenos obtenga acceso en su propio nombre. 

La teoría parece complicada, pero la verdad es más sencillo de lo que parece.

4 Conceptos imprescindibles para OAuth2

/********************************************************************************

1. Roles Oauth2 

********************************************************************************/

1.- Propietario del Recurso - Resource Owner
El propietario del recurso es el usuario que autoriza una aplicación para acceder a su cuenta.
El acceso de la aplicación a la cuenta del usuario esta limitado al alcance de la autorización otorgada.
Ejemplo, acceso solo lectura, acceso a escritura.
Ejemplo Aplicación que muestra datos bancarios.
El propietarios del recurso, es decir nosotros, autorizamos a la aplicación que nos hemos descargado a que nos muestre los movimientos bancarios, pero no le damos permiso a realizar transferencias.

2.- El Servidor de Recursos - Resource Server
El servidor que aloja los recursos protegidos, capaz de aceptar y responder a solicitudes de recursos protegidos utilizando tokens de acceso.
En otras palabras es el servidor que aloja las cuentas de usuarios protegidas y los datos, en el ejemplo seria el servidor del banco donde tenemos dinero y que permite ver los movimientos de la cuenta, pero también realizar transferencias y demás.

3.-El Cliente
Es una aplicación que realiza solicitudes de recursos protegidos en nombre del propietario del recurso y con su autorización.
Es decir el cliente es la aplicación que quiere acceder a la cuenta del usuario. Antes de hacerlo, debe ser autorizado por el Propietario del recurso y la Autorización debe ser validada por la API del Servidor. 
Ejemplo: Puede ser una aplicación móvil, que solicita permiso para acceder a una cuenta bancaria del cliente y poder ver sus movimientos bancarios.

4.- Servidor de Autorizaciones - Authorization Server
El servidor que emite tokens de acceso al cliente después de autenticar con existo el propietario del recurso y obtener autorización.

Desde el punto de vista Back-end los desarrolladores tendrán que crear el Servidor de Recursos y el Servidor de Autorización.

Desde el punto de vista de desarrolladores de Aplicaciones Android o iOS, los desarrolladores tendrán que crear el Cliente.


/********************************************************************************

2. OAuth2 Tipos de Concesión de Autorización - Authorization Grant types

********************************************************************************/

Una Concesión de autorización es una credencial que representa la autorización del propietario del recurso para poder acceder a sus recursos protegidos utilizada por el cliente para obtener un token de acceso.

	1.- Código de Autorización
	
	2.- Implicita
	
	3.- Credenciales de contraseña del propietario del recurso
	
	4.- Credenciales del cliente.
	
En nuestro ejemplo utilizamos la concesión de credenciales de contraseña del propietario del recurso. 

/********************************************************************************

3. Token de OAuth2

********************************************************************************/

Los tokens son cadenas aleatorias específica de la implementación, son generadas por el servidor de autorización y se emiten cuando el cliente las solicita. 
Hay dos clases de token en OAuth2

1.- Token de Acceso: Se envia con cada solicitud, para acceder a un recurso, si el token es valido nos devuelve el recurso sino no. Tiene un tiempo de vida muy corto, ejemplo una hora o menos.

2.- Token de Refresco: Se utiliza para obtener un nuevo token de acceso. No se envía con cada petición al servidor, sino que solo se envía cuando no tenemos el token de acceso o este ha caducado.No se utiliza para obtener recursos del servidor, sino solo un nuevo token de acceso.

Su duración es mayor que el token de acceso y depende de la seguridad que se le quiera dar al proyecto. 

/********************************************************************************

4.- El Scope del Token de Acceso

********************************************************************************/

Es decir que el Usuario da al cliente un acceso especifico a sus recursos. Ejemplo que solo pueda leer, o que también pueda escribir.
En el ejemplo bancario el Usuario solo da acceso a ver sus Movimientos, pero no a hacer operaciones bancarias. 
