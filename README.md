# shopping-cart-app
Una aplicación de microservicios desarrollada en Spring.

Consiste de un microservicio que administra bases de datos de productos disponibles, carritos y sus ítems. Tiene un servicio secundario de usuarios para integrar conceptos de conexiones entre microservicios (específicamente FeignClient).

Emplea una variedad de conceptos y utilidades:
- Patrones de diseño (principalmente Circuit Breaker)
- Genéricos
- Mapeadores
- Manejo de excepciones
- Pruebas unitarias

Específicamente sobre Spring, la aplicación integra:
- Conexiones entre microservicios con FeignClient 
- Un servicio Discovery
- Un servidor de configuración
- El servidor de mensajería RabbitMQ
- Autenticación externa y entre servicios mediante Keycloak
- Integración de Open API (Swagger)

Actualmente estoy estudiando cómo dockerizar la aplicación, y tengo pendiente estudiar pruebas unitarias para la capa de controlador y pruebas de integración.