USE dimo;
-- Registros de base de datos
INSERT INTO `dimo`.`roles` (`id`, `creation_date`, `description`, `name`) VALUES ('1', '20240505', 'Es un rol de administrador', 'Admin');
INSERT INTO `dimo`.`roles` (`id`, `creation_date`, `description`, `name`) VALUES ('2', '20240505', 'Es un rol de Usuario', 'User');
-- Registros con role_id 1
INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (1, '2024-11-22 00:03:37.842969', 'p1@gmail.com', 'nombrePrueba1', 'apellidoPrueba1', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (2, '2024-11-22 01:03:37.842969', 'p2@gmail.com', 'nombrePrueba2', 'apellidoPrueba2', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (3, '2024-11-22 02:03:37.842969', 'p3@gmail.com', 'nombrePrueba3', 'apellidoPrueba3', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (4, '2024-11-22 03:03:37.842969', 'p4@gmail.com', 'nombrePrueba4', 'apellidoPrueba4', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (5, '2024-11-22 04:03:37.842969', 'p5@gmail.com', 'nombrePrueba5', 'apellidoPrueba5', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (6, '2024-11-22 05:03:37.842969', 'p6@gmail.com', 'nombrePrueba6', 'apellidoPrueba6', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (7, '2024-11-22 06:03:37.842969', 'p7@gmail.com', 'nombrePrueba7', 'apellidoPrueba7', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (8, '2024-11-22 07:03:37.842969', 'p8@gmail.com', 'nombrePrueba8', 'apellidoPrueba8', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (9, '2024-11-22 08:03:37.842969', 'p9@gmail.com', 'nombrePrueba9', 'apellidoPrueba9', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (10, '2024-11-22 09:03:37.842969', 'p10@gmail.com', 'nombrePrueba10', 'apellidoPrueba10', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 1);

-- Registros con role_id 2
INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (11, '2024-11-22 10:03:37.842969', 'p11@gmail.com', 'nombrePrueba11', 'apellidoPrueba11', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (12, '2024-11-22 11:03:37.842969', 'p12@gmail.com', 'nombrePrueba12', 'apellidoPrueba12', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (13, '2024-11-22 12:03:37.842969', 'p13@gmail.com', 'nombrePrueba13', 'apellidoPrueba13', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (14, '2024-11-22 13:03:37.842969', 'p14@gmail.com', 'nombrePrueba14', 'apellidoPrueba14', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (15, '2024-11-22 14:03:37.842969', 'p15@gmail.com', 'nombrePrueba15', 'apellidoPrueba15', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (16, '2024-11-22 15:03:37.842969', 'p16@gmail.com', 'nombrePrueba16', 'apellidoPrueba16', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (17, '2024-11-22 16:03:37.842969', 'p17@gmail.com', 'nombrePrueba17', 'apellidoPrueba17', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (18, '2024-11-22 17:03:37.842969', 'p18@gmail.com', 'nombrePrueba18', 'apellidoPrueba18', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (19, '2024-11-22 18:03:37.842969', 'p19@gmail.com', 'nombrePrueba19', 'apellidoPrueba19', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);

INSERT INTO users (id, creation_date, email, first_name, last_name, password, soft_delete, update_date, role_id) 
VALUES (20, '2024-11-22 19:03:37.842969', 'p20@gmail.com', 'nombrePrueba20', 'apellidoPrueba20', '$2a$10$woDXIqvaB0RwSFWGsv4gz.b0VZHz2QBGtm50rzbS/CCHV4NWrmoN6', NULL, NULL, 2);