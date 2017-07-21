--------- INSERT SCRIPTS TO INITIALIZE DATABASE GO HERE --------
INSERT INTO connection_types_gdma2(id, connection_class, name, select_get_tables) VALUES (1, 'com.mysql.jdbc.Driver', 'MySQL (JDBC)', 'SHOW TABLES');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (2,  'select table_schema||''.''||table_name as table_name from information_schema.tables where table_schema not in (''pg_catalog'', ''information_schema'') order by table_schema||''.''||table_name', 'org.postgresql.Driver', 'Postgres 9.6.2 JDBC');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (3,  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE= ''U'' ORDER BY NAME', 'sun.jdbc.odbc.JdbcOdbcDriver', 'MS SQL Server (ODBC)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (4,  'select table_name as TABLE_NAME from user_tables', 'sun.jdbc.odbc.JdbcOdbcDriver', 'Oracle 8/9 (ODBC)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (5,  'select table_name as TABLE_NAME from user_tables', 'oracle.jdbc.OracleDriver', 'Oracle 8/9 (JDBC)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (6,  'SELECT TABLENAME AS TABLE_NAME FROM DBC.TABLES ORDER BY TABLENAME', 'sun.jdbc.odbc.JdbcOdbcDriver', 'Teradata 8/9 (ODBC)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (7, 'SELECT TABLENAME AS TABLE_NAME FROM DBC.TABLES ORDER BY TABLENAME where databasename = ''financial''', 'com.teradata.jdbc.TeraDriver', 'Teradata 8/9 (JDBC)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (8,  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=''U'' ORDER BY NAME', 'net.sourceforge.jtds.jdbc.Driver', 'MS SQL Server (JDBC)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (9,  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=''V'' ORDER BY NAME', 'net.sourceforge.jtds.jdbc.Driver', 'MS SQL Server (JDBC)(Views)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (10,  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE IN (''U'', ''V'') ORDER BY NAME', 'net.sourceforge.jtds.jdbc.Driver', 'MS SQL Server (JDBC)(Both)');
INSERT INTO connection_types_gdma2(id, select_get_tables, connection_class, name) VALUES (11,  'SELECT TABLENAME AS TABLE_NAME FROM DBC.TABLES ORDER BY TABLENAME where databasename = ''gdma_test''', 'com.teradata.jdbc.TeraDriver', 'Teradata 8/9 (JDBC) gen');


SELECT setval('seq_connection_types_gdma2', (SELECT MAX(id) FROM connection_types_gdma2));