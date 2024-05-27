--SESION 2
--CASO READ COMMITTED
--t4
SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
--t6
UPDATE Cuenta
SET saldo = saldo - 30000
WHERE id = 4;
--t8
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 4, 4, 'Retirar', 30000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 982800);
--t9
UPDATE Cuenta
SET saldo = saldo + 5000
WHERE id = 8;
--t10
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 8, 8, 'Consignar', 5000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 5000);
--t12
COMMIT;
--t13
SELECT saldo FROM Cuenta WHERE id = 4;
SELECT saldo FROM Cuenta WHERE id = 8;


--SESION 2
--CASO SERIALIZABLE
--t4
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
--t6
UPDATE Cuenta
SET saldo = saldo - 30000
WHERE id = 13;
--t8
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 15, 13, 'Retirar', 30000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 120000);
--t9
UPDATE Cuenta
SET saldo = saldo + 5000
WHERE id = 11;
--t10
INSERT INTO operacionbancariacuenta (ID, ID_CLIENTE, ID_CUENTA, TipoOperacion, Valor, Fecha, HORA, SALDORESULTANTE)
VALUES (OperacionBancariaCuenta_SEQUENCE.NEXTVAL, 13, 11, 'Consignar', 5000, TO_DATE('2024-04-23', 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24:MI:SS'), 365000);
--t12
Commit;
--t13
SELECT saldo FROM Cuenta WHERE id = 13;
SELECT saldo FROM Cuenta WHERE id = 11;



