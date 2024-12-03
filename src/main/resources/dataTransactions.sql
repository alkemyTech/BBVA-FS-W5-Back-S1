USE dimo;
-- Registros con type DEPOSIT
INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(1, 100000, 'Primer depósito', '2024-11-29 11:00:00.000000', 'deposit', 1);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(2, 75000, 'Segundo depósito', '2024-11-29 11:00:00.000000', 'deposit', 1);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(3, 50000, 'Depósito adicional', '2024-11-29 11:15:00.000000', 'deposit', 2);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(4, 25000, 'Depósito parcial', '2024-11-29 11:30:00.000000', 'deposit', 3);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(5, 30000, 'Depósito por transferencia', '2024-11-29 12:00:00.000000', 'deposit', 1);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(6, 45000, 'Depósito ahorro', '2024-11-29 12:15:00.000000', 'deposit', 2);

-- Registros con type PAYMENT

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(7, 20000, 'Pago de servicio', '2024-11-29 13:00:00.000000', 'payment', 1);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(8, 15000, 'Pago de tarjeta de crédito', '2024-11-29 13:15:00.000000', 'payment', 2);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(9, 18000, 'Pago de préstamo', '2024-11-29 13:30:00.000000', 'payment', 3);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(10, 25000, 'Pago de suscripción', '2024-11-29 13:45:00.000000', 'payment', 1);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(11, 12000, 'Pago de factura', '2024-11-29 14:00:00.000000', 'payment', 2);

INSERT INTO `dimo`.`transactions` (id, amount, description, transaction_date, type, account_id)
VALUES
(12, 30000, 'Pago de alquiler', '2024-11-29 14:30:00.000000', 'payment', 3);



