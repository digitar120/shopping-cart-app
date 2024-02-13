# shopping-cart-app
Una aplicación que simula una API para carritos de compras. Cuenta con funciones para registrar carritos, ítems, productos y usuarios.

Cuenta con:
- Comunicación entre microservicios: La aplicación que registra los carritos e ítems se comunica con otro microservicio que registra usuarios, utilizando FeignClient.
- Servidor de configuración: La aplicación recibe sus configuraciones desde un servidor de configuración.
- Funcionalidades con Discovery Service y servidores de mensajería (RabbitMQ).
- Autenticación con Keycloak
- Enmascarado con una API gateway.

# Objetivos
- Implementar un patrón de resiliencia en las comunicaciones con el servicio de usuarios.
- Pruebas
- Dockerización
