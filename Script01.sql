DROP DATABASE IF EXISTS electrica_rojas;

CREATE DATABASE electrica_rojas;
USE electrica_rojas;

-- =========================
-- TABLA USUARIOS
-- =========================
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL
);

INSERT INTO usuarios (nombre, usuario, password, rol)
VALUES
('Administrador', 'admin', '1234', 'ADMIN'),
('Vendedora', 'vendedora', '1234', 'VENDEDOR');

-- =========================
-- TABLA CATEGORIAS
-- =========================
CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO categorias (nombre)
VALUES
('Cables'),
('Interruptores'),
('Contactos'),
('Iluminacion'),
('Paneles solares'),
('Herramientas'),
('Canalizacion'),
('Protecciones electricas'),
('Otros');

-- =========================
-- TABLA PRODUCTOS
-- =========================
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria)
VALUES 
('Cable calibre 12', 'Cable electrico color rojo', 15.50, 100, 1),
('Apagador sencillo', 'Apagador para instalacion electrica', 25.00, 50, 2),
('Contacto doble', 'Contacto doble color blanco', 35.00, 40, 3),
('Foco LED 9W', 'Foco LED luz blanca', 45.00, 30, 4),
('Panel solar 450W', 'Panel solar monocristalino', 2500.00, 8, 5),
('Centro de carga', 'Centro de carga para 2 pastillas', 180.00, 10, 8);

-- =========================
-- TABLA CLIENTES
-- =========================
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(200)
);

INSERT INTO clientes (nombre, telefono, correo, direccion)
VALUES
('Publico general', '', '', ''),
('Constructora Norte', '6621234567', 'contacto@constructoranorte.com', 'Hermosillo, Sonora'),
('Juan Perez', '6627654321', 'juan@email.com', 'Colonia Centro');

-- =========================
-- TABLA VENTAS
-- =========================
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    id_usuario INT NOT NULL,
    id_cliente INT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

-- =========================
-- TABLA DETALLE_VENTA
-- =========================
CREATE TABLE detalle_venta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- =========================
-- TABLA MERMAS
-- =========================
CREATE TABLE mermas (
    id_merma INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    motivo VARCHAR(200) NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- =========================
-- CONSULTAS DE VERIFICACION
-- =========================
SELECT * FROM usuarios;
SELECT * FROM categorias;
SELECT * FROM productos;
SELECT * FROM clientes;
