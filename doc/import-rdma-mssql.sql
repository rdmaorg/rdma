-- import-rdma-oracle.sql --
-- RDMA IMPORT SCRIPT FOR ORACLE DATABASE --
-- Author: Daniel Serva --

--import users
INSERT INTO RDMA.users_gdma2(active, is_admin, is_locked, first_name, last_name, username, user_domain, user_password)
     SELECT (CASE WHEN ACTIVE = 'Y'
	            THEN 1
				ELSE 0
			END) AS ACTIVE,
			(CASE WHEN ISADMIN = 'Y'
	            THEN 1
				ELSE 0
			END) AS ISADMIN,
			(CASE WHEN ISLOCKED = 'Y'
	            THEN 1
				ELSE 0
			END) AS ISLOCKED,
				FIRST_NAME, LAST_NAME, USERNAME, '', '' FROM GDMA2_USERS;

--import connection types
INSERT INTO RDMA.dbo.connection_types_gdma2(select_get_tables, connection_class, name) 
       select select_get_tables, connectionclass, name from GDMA.dbo.gdma2_odbc_types;
			
-- import server_gdma2
  INSERT INTO RDMA.dbo.server_gdma2
           (active
           ,url
           ,name
           ,password
           ,prefix
           ,username
           ,connection_type_id)
     SELECT (CASE WHEN ACTIVE = 'Y'
	            THEN 1
				ELSE 0
			END) AS ACTIVE
      ,URL
      ,NAME
      ,PASSWORD
      ,PREFIX
      ,USERNAME
      ,ODBC_TYPE_ID
  FROM GDMA.dbo.GDMA2_SERVER_REGISTRATION;
  
-- import table_gdma2
INSERT INTO RDMA.dbo.table_gdma2
           (active
           ,table_alias
           ,name
           ,server_id)
SELECT (CASE WHEN ACTIVE = 'Y'
	            THEN 1
				ELSE 0
			END) AS ACTIVE
      ,NAME as ALIAS
      ,NAME
      ,SERVER_ID
  FROM GDMA.dbo.GDMA2_TABLE;
  
-- import column
INSERT INTO RDMA.dbo.column_gdma2
           (active
           ,column_alias
           ,allow_insert
           ,allow_update
           ,column_size
           ,column_type
           ,column_type_str
           ,displayed
           ,max_width
           ,min_width
           ,name
           ,is_nullable
           ,order_by
           ,is_primary_key
           ,special
           ,dd_lookup_display
           ,dd_lookup_store
           ,table_id)
SELECT (CASE WHEN ACTIVE = 'Y'
	            THEN 1
				ELSE 0
			END) AS ACTIVE
      ,NAME AS ALIAS
      ,(CASE WHEN ALLOWINSERT = 'Y'
	            THEN 1
				ELSE 0
			END) AS ALLOWINSERT
      ,(CASE WHEN ALLOWUPDATE = 'Y'
	            THEN 1
				ELSE 0
			END) AS ALLOWUPDATE
      ,COLUMN_SIZE
      ,TYPE
      ,TYPESTRING
	  ,(CASE WHEN DISPLAYED = 'Y'
	            THEN 1
				ELSE 0
			END) AS DISPLAYED
      ,MAXWIDTH
      ,MINWIDTH
      ,NAME
      ,(CASE WHEN NULLABLE = 'Y'
	            THEN 1
				ELSE 0
			END) AS NULLABLE
      ,ORDERBY
	  ,(CASE WHEN PRIMARYKEY = 'Y'
	            THEN 1
				ELSE 0
			END) AS PRIMARYKEY
	  ,(CASE WHEN SPECIAL = 'Y'
	            THEN 1
				ELSE 0
			END) AS SPECIAL
	  ,DD_LOOKUP_COLUMN_DISPLAY
      ,DD_LOOKUP_COLUMN_STORE
      ,TABLE_ID
  FROM GDMA.dbo.GDMA2_COLUMN;
  
-- import user_table_access_gdma2
INSERT INTO RDMA.dbo.user_table_access_gdma2(table_id, user_id, allow_display, allow_update, allow_insert, allow_delete)
SELECT TABLE_ID, USER_ID
	  ,(CASE WHEN ALLOW_DISPLAY = 'Y'
	            THEN 1
				ELSE 0
			END) AS ALLOW_DISPLAY,
	  (CASE WHEN ALLOW_UPDATE = 'Y'
	            THEN 1
				ELSE 0
			END) AS ALLOW_UPDATE,
	  (CASE WHEN ALLOW_INSERT = 'Y'
	            THEN 1
				ELSE 0
			END) AS ALLOW_INSERT,	  
	  (CASE WHEN ALLOW_DELETE = 'Y'
	            THEN 1
				ELSE 0
			END) AS ALLOW_DELETE
  FROM GDMA.dbo.GDMA2_USER_TABLE_ACCESS;