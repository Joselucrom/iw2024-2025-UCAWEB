
insert into application_user (version, id, email, username, name, hashed_password, profile_picture) values
    (1, 1, 'carlos.romero@university.com', 'carlos.romero', 'Carlos Romero', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 2, 'laura.diaz@university.com', 'laura.diaz', 'Laura Díaz', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 3, 'miguel.torres@university.com', 'miguel.torres', 'Miguel Ángel Torres', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 4, 'elena.sanchez@university.com', 'elena.sanchez', 'Elena Sánchez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 5, 'juan.herrera@university.com', 'juan.herrera', 'Juan Pablo Herrera', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 6, 'maria.gonzalez@university.com', 'maria.gonzalez', 'María González', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 7, 'jose.martin@university.com', 'jose.martin', 'José Luis Martín', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 8, 'isabel.lopez@university.com', 'isabel.lopez', 'Isabel López', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 9, 'antonio.garcia@university.com', 'antonio.garcia', 'Antonio García', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 10, 'sofia.perez@university.com', 'sofia.perez', 'Sofía Pérez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 11, 'ricardo.fernandez@university.com', 'ricardo.fernandez', 'Ricardo Fernández', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 12, 'paula.ruiz@university.com', 'paula.ruiz', 'Paula Ruiz', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 13, 'fernando.jimenez@university.com', 'fernando.jimenez', 'Fernando Jiménez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 14, 'lidia.sanchez@university.com', 'lidia.sanchez', 'Lidia Sánchez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 15, 'alberto.martin@university.com', 'alberto.martin', 'Alberto Martín', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 16, 'carmen.rodriguez@university.com', 'carmen.rodriguez', 'Carmen Rodríguez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 17, 'david.perez@university.com', 'david.perez', 'David Pérez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 18, 'esther.garcia@university.com', 'esther.garcia', 'Esther García', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 19, 'javier.mendoza@university.com', 'javier.mendoza', 'Javier Mendoza', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
    (1, 20, 'beatriz.martinez@university.com', 'beatriz.martinez', 'Beatriz Martínez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', '');

insert into user_roles (user_id, roles) values
(1, 'USER'),
(2, 'USER'),
(3, 'USER'),
(4, 'USER'),
(5, 'USER'),
(6, 'USER'),
(7, 'USER'),
(8, 'USER'),
(9, 'USER'),
(10, 'USER'),
(11, 'USER'),
(12, 'USER'),
(13, 'USER'),
(14, 'USER'),
(15, 'USER'),
(16, 'USER'),
(17, 'USER'),
(18, 'USER'),
(19, 'USER'),
(20, 'USER');
insert into application_user (version, id, email, username,name,hashed_password,profile_picture) values (1, '21', 'hola@adios.com', 'user','John Normal','$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe','');
insert into user_roles (user_id, roles) values ('21', 'USER');
insert into application_user (version, id, email, username,name,hashed_password,profile_picture) values (1, '22', 'admin@admin.adm', 'admin','Emma Executive','$2a$10$jpLNVNeA7Ar/ZQ2DKbKCm.MuT2ESe.Qop96jipKMq7RaUgCoQedV.','');
insert into user_roles (user_id, roles) values ('22', 'USER');
insert into user_roles (user_id, roles) values ('22', 'ADMIN');
insert into user_roles (user_id, roles) values ('22', 'CIO');
insert into user_roles (user_id, roles) values ('22', 'PROMOTOR');




insert into application_user (version, id, email, username, name, hashed_password, profile_picture) values
(1, 'u00000001', 'carlos.romero@university.com', 'carlos.romero', 'Carlos Romero', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000002', 'laura.diaz@university.com', 'laura.diaz', 'Laura Díaz', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000003', 'miguel.torres@university.com', 'miguel.torres', 'Miguel Ángel Torres', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000004', 'elena.sanchez@university.com', 'elena.sanchez', 'Elena Sánchez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000005', 'juan.herrera@university.com', 'juan.herrera', 'Juan Pablo Herrera', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000006', 'maria.gonzalez@university.com', 'maria.gonzalez', 'María González', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000007', 'jose.martin@university.com', 'jose.martin', 'José Luis Martín', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000008', 'isabel.lopez@university.com', 'isabel.lopez', 'Isabel López', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000009', 'antonio.garcia@university.com', 'antonio.garcia', 'Antonio García', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000010', 'sofia.perez@university.com', 'sofia.perez', 'Sofía Pérez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000011', 'ricardo.fernandez@university.com', 'ricardo.fernandez', 'Ricardo Fernández', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000012', 'paula.ruiz@university.com', 'paula.ruiz', 'Paula Ruiz', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000013', 'fernando.jimenez@university.com', 'fernando.jimenez', 'Fernando Jiménez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000014', 'lidia.sanchez@university.com', 'lidia.sanchez', 'Lidia Sánchez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000015', 'alberto.martin@university.com', 'alberto.martin', 'Alberto Martín', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000016', 'carmen.rodriguez@university.com', 'carmen.rodriguez', 'Carmen Rodríguez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000017', 'david.perez@university.com', 'david.perez', 'David Pérez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000018', 'esther.garcia@university.com', 'esther.garcia', 'Esther García', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000019', 'javier.mendoza@university.com', 'javier.mendoza', 'Javier Mendoza', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', ''),
(1, 'u00000020', 'beatriz.martinez@university.com', 'beatriz.martinez', 'Beatriz Martínez', '$2a$10$xdbKoM48VySZqVSU/cSlVeJn0Z04XCZ7KZBjUBC00eKo5uLswyOpe', '');

-- Roles para cada usuario
insert into user_roles (user_id, roles) values
('u00000001', 'USER'),
('u00000002', 'USER'),
('u00000003', 'USER'),
('u00000004', 'USER'),
('u00000005', 'USER'),
('u00000006', 'USER'),
('u00000007', 'USER'),
('u00000008', 'USER'),
('u00000009', 'USER'),
('u00000010', 'USER'),
('u00000011', 'USER'),
('u00000012', 'USER'),
('u00000013', 'USER'),
('u00000014', 'USER'),
('u00000015', 'USER'),
('u00000016', 'USER'),
('u00000017', 'USER'),
('u00000018', 'USER'),
('u00000019', 'USER'),
('u00000020', 'USER');



DROP TABLE user_roles;
DROP TABLE application_user;
