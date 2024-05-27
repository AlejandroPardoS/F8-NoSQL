--SESION 1
--CASO READ COMMITTED
--t1
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
--t2
UPDATE Cuenta
SET saldo = saldo + 1000000
WHERE id = 4;
--t3
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 4, 4, 'Consignar', 1000000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 1012800);
--t4
UPDATE Cuenta
SET saldo = saldo - 50000
WHERE id = 8;
--t5
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 8, 8, 'Retirar', 50000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 0);
--t7
COMMIT;
--t11
SELECT saldo FROM Cuenta WHERE id = 4;
SELECT saldo FROM Cuenta WHERE id = 8;
--t13
SELECT saldo FROM Cuenta WHERE id = 4;
SELECT saldo FROM Cuenta WHERE id = 8;


--SESION 1
--CASO SERIALIZABLE
--t1
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
--t2
UPDATE Cuenta
SET saldo = saldo + 1000000
WHERE id = 13;
--t3
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 15, 13, 'Consignar', 1000000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 1150000);
--t4
UPDATE Cuenta
SET saldo = saldo - 50000
WHERE id = 11;
--t5
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 13, 11, 'Retirar', 50000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 360000);
--t7
COMMIT;
--t11
SELECT saldo FROM Cuenta WHERE id = 13;
SELECT saldo FROM Cuenta WHERE id = 11;
--t13
SELECT saldo FROM Cuenta WHERE id = 13;
SELECT saldo FROM Cuenta WHERE id = 11;



