DROP DATABASE IF EXISTS electrica_rojas;
CREATE DATABASE electrica_rojas;
USE electrica_rojas;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

INSERT INTO usuarios (nombre, usuario, password, rol) VALUES
('Administrador', 'admin', '1234', 'ADMIN'),
('Isela Rojas', 'vendedora', '1234', 'VENDEDOR');

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO categorias (nombre) VALUES
('Cables'),
('Interruptores'),
('Contactos'),
('Iluminacion'),
('Paneles solares'),
('Protecciones electricas'),
('Herramientas'),
('Canalizacion'),
('Accesorios');

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria) VALUES
('Cable calibre 12', 'Cable electrico color rojo', 15.50, 105, 1),
('Apagador sencillo', 'Apagador para instalacion electrica', 25.00, 54, 2),
('Contacto doble', 'Contacto doble color blanco', 35.00, 43, 3),
('Foco LED 9W', 'Foco LED luz blanca', 45.00, 69, 4),
('Panel solar 450W', 'Panel solar monocristalino', 2500.00, 8, 5),
('Centro de carga', 'Centro de carga para 2 pastillas', 180.00, 14, 6),
('Cable calibre 10', 'Cable electrico uso rudo', 18.75, 80, 1),
('Breaker 30A', 'Interruptor termomagnetico 30 amperes', 95.00, 33, 6),
('Cinta aislar negra', 'Cinta aislante para uso electrico', 22.50, 97, 9),
('Tubo conduit 1/2', 'Tubo conduit PVC para instalacion', 32.00, 60, 8),
('Desarmador plano', 'Herramienta manual aislada', 65.00, 40, 7),
('Multimetro digital', 'Multimetro para mediciones electricas', 380.00, 12, 7),
('Regleta multicontacto', 'Regleta de 6 entradas', 120.00, 7, 3);

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(200)
);

INSERT INTO clientes (nombre, telefono, correo, direccion) VALUES
('Publico general', '', '', ''),
('Constructora Norte', '6621234567', 'compras@constructoranorte.com', 'Hermosillo, Sonora'),
('Juan Perez', '6622223344', 'juan.perez@gmail.com', 'Colonia Centro'),
('Taller Electrico Lopez', '6625557788', 'lopez@taller.com', 'Blvd. Solidaridad'),
('Rancho El Mezquite', '6628881122', 'rancho@mezquite.com', 'Carretera Kino km 28'),
('Hotel Sol del Desierto', '6624449900', 'mantenimiento@hotelsol.com', 'Zona hotelera');

CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2),
    id_usuario INT,
    id_cliente INT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE TABLE detalle_venta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT,
    id_producto INT,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

INSERT INTO ventas (fecha, total, id_usuario, id_cliente) VALUES
('2026-03-15 10:20:00', 149.00, 2, 3),
('2026-04-01 09:15:00', 127.50, 2, NULL),
('2026-04-03 12:35:00', 320.00, 2, 2),
('2026-04-07 15:10:00', 337.50, 2, 4),
('2026-04-12 11:45:00', 2687.50, 1, 5),
('2026-04-18 16:30:00', 335.00, 2, 6),
('2026-04-21 13:05:00', 436.00, 2, NULL);

INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio) VALUES
(1, 1, 8, 15.50),
(1, 2, 1, 25.00),
(2, 1, 5, 15.50),
(2, 2, 2, 25.00),
(3, 6, 1, 180.00),
(3, 3, 4, 35.00),
(4, 4, 6, 45.00),
(4, 9, 3, 22.50),
(5, 5, 1, 2500.00),
(5, 7, 10, 18.75),
(6, 2, 3, 25.00),
(6, 3, 2, 35.00),
(6, 8, 2, 95.00),
(7, 4, 4, 45.00),
(7, 10, 8, 32.00);

CREATE TABLE mermas (
    id_merma INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    cantidad INT NOT NULL,
    motivo VARCHAR(200),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

INSERT INTO mermas (id_producto, cantidad, motivo, fecha) VALUES
(1, 2, 'Cable danado durante acomodo de almacen', '2026-04-02 08:30:00'),
(4, 1, 'Foco quebrado en exhibicion', '2026-04-05 17:20:00'),
(5, 1, 'Panel con golpe en traslado', '2026-04-10 10:10:00'),
(10, 2, 'Tubo roto durante descarga', '2026-04-16 09:40:00'),
(3, 1, 'Contacto con defecto fisico', '2026-04-20 14:15:00');

SELECT 'Usuarios' AS tabla;
SELECT id_usuario, nombre, usuario, rol FROM usuarios;

SELECT 'Productos' AS tabla;
SELECT p.nombre, c.nombre AS categoria, p.precio, p.stock
FROM productos p
INNER JOIN categorias c ON p.id_categoria = c.id_categoria
ORDER BY p.nombre;

SELECT 'Ventas' AS tabla;
SELECT v.id_venta, v.fecha, v.total, COALESCE(c.nombre, 'Publico general') AS cliente
FROM ventas v
LEFT JOIN clientes c ON v.id_cliente = c.id_cliente
ORDER BY v.id_venta;

SELECT 'Mermas' AS tabla;
SELECT m.fecha, p.nombre AS producto, m.cantidad, m.motivo
FROM mermas m
INNER JOIN productos p ON m.id_producto = p.id_producto
ORDER BY m.fecha;
