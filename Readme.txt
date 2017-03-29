RECREATING DB ON LOCAL : 

-  If domain objects and @Entity as heavliy changed you need to recreate DB

1. Manually delete DB using some tool and create it again. This way, no dirty tables will stay from previous DB creation
2. 	change file application.properties
		
		#spring.jpa.hibernate.ddl-auto=update
		spring.jpa.hibernate.ddl-auto=create-drop

... use 'create-drop' just this one time, and if DB creation is as expected, comment and use 'update' 
 

=======

	Hibernate logging:

- application.properties:
	spring.jpa.properties.hibernate.show_sql=true
	spring.jpa.properties.hibernate.use_sql_comments=true
	spring.jpa.properties.hibernate.format_sql=true
	spring.jpa.properties.hibernate.type=trace 
	
- log4j.xml 
	
	change from 
		
		<root>
			<level value="ERROT" />	
	
	to
		<root>
			<level value="DEBUG" />

Now you can see Hibernate log msgs in application log file: 
	gdma2-web.log			


=====
POSTGRES - DATA LOAD:
TODO: replace IDs with sequencers 

POSTGRES - DATA LOAD:





INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'select * from information_schema.tables WHERE table_schema=''public''', 'org.postgresql.Driver', 'Postgres 9.6.2 JDBC');

INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'SHOW TABLES', 'com.mysql.jdbc.Driver', 'MySQL (JDBC)');

INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE= ''U'' ORDER BY NAME', 'sun.jdbc.odbc.JdbcOdbcDriver', 'MS SQL Server (ODBC)');

INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'select table_name as TABLE_NAME from user_tables', 'sun.jdbc.odbc.JdbcOdbcDriver', 'Oracle 8/9 (ODBC)');

INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'select table_name as TABLE_NAME from user_tables', 'oracle.jdbc.OracleDriver', 'Oracle 8/9 (JDBC)');

INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'SELECT TABLENAME AS TABLE_NAME FROM DBC.TABLES ORDER BY TABLENAME', 'sun.jdbc.odbc.JdbcOdbcDriver', 'Teradata 8/9 (ODBC)');



INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'), 'SELECT TABLENAME AS TABLE_NAME FROM DBC.TABLES ORDER BY TABLENAME where databasename = ''financial''', 'com.teradata.jdbc.TeraDriver', 'Teradata 8/9 (JDBC)');


INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=''U'' ORDER BY NAME', 'net.sourceforge.jtds.jdbc.Driver', 'MS SQL Server (JDBC)');


INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=''V'' ORDER BY NAME', 'net.sourceforge.jtds.jdbc.Driver', 'MS SQL Server (JDBC)(Views)');

INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE IN (''U'', ''V'') ORDER BY NAME', 'net.sourceforge.jtds.jdbc.Driver', 'MS SQL Server (JDBC)(Both)');

INSERT INTO connection_types_gdma2(
            id, select_get_tables, connection_class, name)
    VALUES (nextval('seq_connection_types_gdma2'),  'SELECT TABLENAME AS TABLE_NAME FROM DBC.TABLES ORDER BY TABLENAME where databasename = ''gdma_test''', 'com.teradata.jdbc.TeraDriver', 'Teradata 8/9 (JDBC) gen');






INSERT INTO "server_gdma2" VALUES (nextval('seq_server_gdma2'), false, 'TEST alias1','jdbc:pgsql://localhost:5432/gdma20','TEST POSTGRES SERVER 1', 'admin', 'dbo', 'postgres1', 1);
INSERT INTO "server_gdma2" VALUES (nextval('seq_server_gdma2'), true, 'TEST alias2', 'jdbc:pgsql://localhost:5432/gdma21','TEST POSTGRES SERVER 2', '12345678', 'public', 'postgres11', 1);
INSERT INTO "server_gdma2" VALUES (nextval('seq_server_gdma2'), true, 'TEST POSTGRES SERVER 3','jdbc:pgsql://localhost:5432/gdma22','TEST alias3', 'querty123', 'dbo', 'postgres22', 2);


INSERT INTO "table_gdma2" VALUES (nextval('seq_table_gdma2'), true,'table alias1', 'USER_VIDEO', 2);
INSERT INTO "table_gdma2" VALUES (nextval('seq_table_gdma2'), true,'table alias2', 'USER_MOVIE2', 2);
INSERT INTO "table_gdma2" VALUES (nextval('seq_table_gdma2'), true,'table alias3', '22test_table', 1);
INSERT INTO "table_gdma2" VALUES (nextval('seq_table_gdma2'), false,'table alias4', 'USER_MOVIE', 2);
INSERT INTO "table_gdma2" VALUES (nextval('seq_table_gdma2'), true,'table alias5', 'Cars', 2);
INSERT INTO "table_gdma2" VALUES (nextval('seq_table_gdma2'), true,'table alias5', 'Tires', 1);



INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'babycenter.com', 'Kelly', 'Smit', true, 'Kelly.J.Smit@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'capitalone.com', 'Jeremy', 'Wilson', false, 'Jeremy.F.Wilson@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'hotels.com', 'George', 'Jones', true, 'George.T.Jones@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'columbia.edu', 'Gertrude', 'Conner', false, 'Gertrude.W.Conner@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'kioskea.net', 'David', 'Engler', false, 'David.K.Engler@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'verizon.com', 'Tracy', 'Guidry', true, 'Tracy.M.Guidry@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'kayak.com', 'Kaye', 'Sherwood', false, 'Kaye.F.Sherwood@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'barnesandnoble.com', 'Carol', 'House', true, 'Carol.P.House@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'wix.com', 'Irma', 'Mccormack', false, 'Irma.R.Mccormack@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'break.com', 'Emma', 'Shafer', true, 'Emma.R.Shafer@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'systweak.com', 'Charles', 'Cleghorn', false, 'Charles.E.Cleghorn@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'wigetmedia.com', 'Florence', 'Simpkins', true, 'Florence.C.Simpkins@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'city-data.com', 'Jason', 'Nelson', false, 'Jason.B.Nelson@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'wsj.com', 'Robt', 'Cox', true, 'Robt.H.Cox@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'aa.com', 'Lauren', 'Mchugh', false, 'Lauren.L.Mchugh@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'staples.com', 'Christina', 'Barber', true, 'Christina.C.Barber@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'tvguide.com', 'Claude', 'Lafayette', false, 'Claude.M.Lafayette@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 't-mobile.com', 'William', 'Chase', true, 'William.M.Chase@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'edmunds.com', 'Mireya', 'Bruton', false, 'Mireya.J.Bruton@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'spokeo.com', 'Patsy', 'Wozniak', true, 'Patsy.F.Wozniak@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'aliexpress.com', 'Maurice', 'Nagata', false, 'Maurice.A.Nagata@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'grindtv.com', 'Charles', 'Dews', true, 'Charles.P.Dews@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'wiktionary.org', 'Elizabeth', 'Oliver', false, 'Elizabeth.B.Oliver@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'oracle.com', 'Jason', 'Parks', true, 'Jason.L.Parks@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'stackexchange.com', 'Donna', 'Childers', false, 'Donna.M.Childers@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'blogspot.ca', 'Thomas', 'Jackson', true, 'Thomas.J.Jackson@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'bankrate.com', 'Jessica', 'Maynard', false, 'Jessica.A.Maynard@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'bizrate.com', 'Eric', 'Gottschalk', true, 'Eric.A.Gottschalk@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'kbb.com', 'Jennifer', 'Springer', false, 'Jennifer.D.Springer@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'justin.tv', 'Daisy', 'Speece', true, 'Daisy.W.Speece@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'toptenreviews.com', 'Betty', 'Hill', false, 'Betty.H.Hill@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'craigslist.ca', 'Erin', 'Brothers', true, 'Erin.R.Brothers@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'sweetim.com', 'Jesse', 'Poole', false, 'Jesse.K.Poole@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'sfgate.com', 'Barbara', 'Black', true, 'Barbara.A.Black@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'imgur.com', 'Dimple', 'Gutierez', false, 'Dimple.P.Gutierez@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'webcrawler.com', 'Denise', 'Koch', true, 'Denise.M.Koch@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'food.com', 'Jeffrey', 'Martinez', false, 'Jeffrey.C.Martinez@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'justanswer.com', 'Anthony', 'Chee', true, 'Anthony.L.Chee@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'lenovo.com', 'Paul', 'French', false, 'Paul.D.French@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'charter.net', 'Anthony', 'Thomas', true, 'Anthony.C.Thomas@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'drugs.com', 'Daniel', 'Wilson', false, 'Daniel.I.Wilson@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'state.gov', 'Mike', 'Rodney', true, 'Mike.T.Rodney@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'thepostgame.com', 'Russell', 'Moore', false, 'Russell.J.Moore@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'verizon.net', 'Raymond', 'Smith', true, 'Raymond.L.Smith@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'kijiji.ca', 'Brenda', 'Niven', false, 'Brenda.E.Niven@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'qq.com', 'Floyd', 'Wheeler', true, 'Floyd.D.Wheeler@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, '4chan.org', 'Kimberly', 'Kania', false, 'Kimberly.R.Kania@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'pbs.org', 'Randall', 'Barry', true, 'Randall.C.Barry@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'tomshardware.com', 'Suzanne', 'Fontenot', false, 'Suzanne.B.Fontenot@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'superpages.com', 'Donna', 'Humphreys', true, 'Donna.P.Humphreys@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'ebay.ca', 'Elizabeth', 'Bain', false, 'Elizabeth.J.Bain@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'skillpages.com', 'Camilla', 'Lehto', true, 'Camilla.H.Lehto@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'wired.com', 'Jeffrey', 'Dennis', false, 'Jeffrey.M.Dennis@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'tigerdirect.com', 'Kelley', 'Melvin', true, 'Kelley.C.Melvin@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'businessinsider.com', 'Perla', 'Keller', false, 'Perla.E.Keller@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'alexa.com', 'Elizabeth', 'Anderson', true, 'Elizabeth.H.Anderson@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'minecraftforum.net', 'Vicki', 'Day', false, 'Vicki.J.Day@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'lumosity.com', 'Beulah', 'Weatherholt', true, 'Beulah.M.Weatherholt@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'coolmath-games.com', 'James', 'Pridmore', false, 'James.K.Pridmore@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'costco.com', 'Ted', 'Roush', true, 'Ted.A.Roush@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'hgtv.com', 'Margaret', 'Rivers', false, 'Margaret.L.Rivers@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'goodgamestudios.com', 'Leroy', 'Hawthorne', true, 'Leroy.O.Hawthorne@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'wwe.com', 'Frances', 'Games', false, 'Frances.D.Games@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'xbox.com', 'Violet', 'Chau', true, 'Violet.W.Chau@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'sony.com', 'Cleveland', 'Courts', false, 'Cleveland.T.Courts@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'tv.com', 'Jill', 'Caldwell', true, 'Jill.G.Caldwell@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'cvs.com', 'Roderick', 'Reynolds', false, 'Roderick.L.Reynolds@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'wordreference.com', 'Gilbert', 'Perry', true, 'Gilbert.N.Perry@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'searchnu.com', 'Kevin', 'Barclay', false, 'Kevin.E.Barclay@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'thedailybeast.com', 'Mark', 'Brown', true, 'Mark.C.Brown@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'ew.com', 'Dorine', 'Cress', false, 'Dorine.A.Cress@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'orbitz.com', 'Elyse', 'Didonato', true, 'Elyse.D.Didonato@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'sbnation.com', 'Hermelinda', 'Phillips', false, 'Hermelinda.R.Phillips@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'kmart.com', 'Annette', 'Bouknight', true, 'Annette.H.Bouknight@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'sparkpeople.com', 'Richard', 'Dorgan', false, 'Richard.S.Dorgan@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'iminent.com', 'Ronald', 'Patton', true, 'Ronald.G.Patton@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'salon.com', 'David', 'Glynn', false, 'David.B.Glynn@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'superuser.com', 'Thomas', 'Cisco', true, 'Thomas.K.Cisco@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'dhgate.com', 'Andrew', 'Wilson', false, 'Andrew.A.Wilson@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'samsclub.com', 'Kenneth', 'Ortega', true, 'Kenneth.S.Ortega@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'cnbc.com', 'Melissa', 'Romero', false, 'Melissa.K.Romero@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'travelzoo.com', 'Scott', 'Hall', true, 'Scott.K.Hall@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'lg.com', 'Nicole', 'Kessler', false, 'Nicole.R.Kessler@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'newsmax.com', 'Ronald', 'Byerly', true, 'Ronald.J.Byerly@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'vid2c.com', 'Cindy', 'White', false, 'Cindy.A.White@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'homes.com', 'Richard', 'Alvarado', true, 'Richard.J.Alvarado@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'badoo.com', 'Laura', 'Vaughn', false, 'Laura.C.Vaughn@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'fool.com', 'Adrienne', 'Thompson', true, 'Adrienne.E.Thompson@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'instantcheckmate.com', 'Richard', 'Cummings', false, 'Richard.M.Cummings@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'gamespot.com', 'Catherine', 'Smith', true, 'Catherine.L.Smith@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'wimp.com', 'Marcie', 'Hoover', false, 'Marcie.J.Hoover@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'sportingnews.com', 'Ima', 'Bowen', true, 'Ima.R.Bowen@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'minecraftwiki.net', 'David', 'Nichols', false, 'David.K.Nichols@spambob.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'bearshare.com', 'Sandra', 'Avila', true, 'Sandra.D.Avila@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'yardbarker.com', 'Christopher', 'Hall', false, 'Christopher.I.Hall@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'cafepress.com', 'Lawrence', 'Sullivan', true, 'Lawrence.E.Sullivan@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'blackberry.com', 'John', 'Rodriguez', false, 'John.R.Rodriguez@mailinator.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'made-in-china.com', 'Angel', 'Martin', true, 'Angel.M.Martin@dodgit.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, false, 'sprint.com', 'Alvin', 'Acevedo', false, 'Alvin.P.Acevedo@trashymail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'ovguide.com', 'Leroy', 'Marvel', true, 'Leroy.C.Marvel@pookmail.com');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, true, 'TD', 'Alan', 'May', true, 'may.a');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), false, true, 'ab', 'Ben', 'Otiss', false, 'otiss.b.');
INSERT INTO "users_gdma2" VALUES (nextval('seq_users_gdma2'), true, false, 'e', 'Mike', 'Mills', false, 'mils.s');



INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1', true, true, 14, 6, '#3D3C3A', true, 69, 6, 'Project Manager', true, 2, true, 'ckUkf', NULL, NULL, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1', false, false, 14, 9, '#463E3F', false, 29, 12, 'Project Support, Business Intelligence and Fraud Agents', false, 2, false, 'YrCzy', NULL, NULL, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1', true, true, 18, 7, '#4C4646', true, 45, 5, 'Proposal Engineering Coordinator', true, 1, true, 'tVOp6', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 17, 2, '#F70D1A', false, 52, 10, 'Yoga Teacher', false, 1, false, 'gpSAl', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 14, 9, '#504A4B', false, 82, 7, 'Protection Engineer', false, 1, false, '7uSGV', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 10, 6, '#565051', true, 89, 15, 'Provisioning Agents', true, 2, true, 'KKZBG', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 11, 6, '#5C5858', false, 71, 15, 'Public Relations Coordinator', false, 1, false, 'eltvL', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 11, 3, '#625D5D', true, 29, 10, 'Public Relations Director', true, 1, true, 'IomIW', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 11, 8, '#666362', false, 77, 7, 'Public Relations Manager', false, 2, false, 'lAUJq', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 5, '#6D6968', true, 70, 12, 'Publicist', true, 2, true, 'gHHsu', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 14, 7, '#726E6D', false, 80, 13, 'Punch Press Operator', false, 2, false, 'XJUD0', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 19, 5, '#736F6E', true, 26, 20, 'QA Product Technical Manager', true, 2, true, 'EtvYK', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 19, 3, '#837E7C', false, 25, 18, 'Quality Assistant', false, 1, false, 'HLSHI', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 10, 5, '#848482', true, 58, 20, 'Quality Associate/Validation', true, 1, true, '6v0nY', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 12, 9, '#B6B6B4', false, 48, 19, 'Quality Assurance Agent', false, 1, false, 'fAweV', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 11, 8, '#D1D0CE', true, 41, 18, 'Quality Assurance Coordinator', true, 2, true, 'y6ztl', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 20, 9, '#E5E4E2', false, 64, 9, 'Quality Assurance Engineer', false, 1, false, 'dlxGz', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 19, 8, '#BCC6CC', true, 63, 8, 'Quality Assurance Manager', true, 1, true, 'SNo3B', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 15, 4, '#98AFC7', false, 55, 20, 'Quality Assurance Manager', false, 1, false, '8BvLv', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 18, 6, '#6D7B8D', true, 20, 14, 'Quality Assurance Technologist', true, 1, true, 'NUqsX', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 16, 9, '#657383', false, 67, 6, 'Quality Control Analyst', false, 1, false, 'FLLib', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 2, '#616D7E', true, 48, 5, 'Quality Control Associate', true, 2, true, 'jXiBe', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 18, 1, '#646D7E', false, 43, 17, 'Quality Control Engineer', false, 1, false, 'Aq4Dd', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 11, 10, '#566D7E', true, 59, 11, 'Quality Control Manager', true, 2, true, 'QKsm0', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 17, 3, '#737CA1', false, 56, 19, 'Quality Control Supervisor', false, 1, false, 'eFUy4', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 13, 4, '#4863A0', true, 91, 6, 'Quality Engineer', true, 2, true, 'sHQV8', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 15, 8, '#2B547E', false, 42, 14, 'R&D Engineer', false, 1, false, 'CMO8w', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 11, 10, '#2B3856', true, 81, 18, 'Ramp Agents', true, 2, true, 'L3YvI', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 17, 7, '#151B54', false, 42, 18, 'Ramp Operator', false, 2, false, 'iJETi', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 6, '#000080', true, 31, 11, 'Ratings Analyst', true, 1, true, 'mmdwM', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 13, 2, '#342D7E', false, 31, 13, 'Real Estate Appraiser', false, 1, false, 'KJDBC', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 16, 7, '#15317E', true, 50, 10, 'Real Estate Officer', true, 1, true, 'FC6Xa', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 12, 10, '#151B8D', false, 67, 20, 'Rebar Detailer', false, 1, false, 'JSxWb', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 19, 2, '#0000A0', true, 49, 19, 'Receptionist', true, 2, true, 'QQFib', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 14, 6, '#0020C2', false, 63, 17, 'Recruiter', false, 1, false, 'aqFo1', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 10, 7, '#0041C2', true, 27, 7, 'Recruitment Consultant', true, 1, true, 't21Fe', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 18, 5, '#2554C7', false, 60, 5, 'Referee', false, 2, false, '6qqRb', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 4, '#1569C7', true, 61, 11, 'Refrigeration Technician', true, 2, true, 'X78g7', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 16, 10, '#2B60DE', false, 90, 17, 'Regional Accounts Trainer', false, 1, false, 'fz7p0', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 19, 7, '#1F45FC', true, 72, 7, 'Regional Sales Manager', true, 1, true, 'UZMPr', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 10, 2, '#6960EC', false, 41, 17, 'Regulatory Affairs Associate', false, 2, false, 'kplSB', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 10, '#736AFF', true, 85, 9, 'Regulatory Affairs Specialist', true, 2, true, 'D2CQO', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 12, 3, '#357EC7', false, 34, 14, 'Rehabilitation Engineering Assistant', false, 1, false, 'XF7AL', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 13, 8, '#368BC1', true, 56, 17, 'Reimbursement Analyst', true, 1, true, 'jMj2h', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 12, 3, '#488AC7', false, 31, 20, 'Relay Engineer', false, 2, false, 'k3RSq', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 15, 2, '#3090C7', true, 60, 10, 'Reliability Engineer', true, 1, true, 'uswOr', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 11, 10, '#659EC7', false, 35, 13, 'Rental Representative', false, 2, false, 'm7Zex', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 10, 4, '#87AFC7', true, 85, 10, 'Report and Visualization Developer, Business Intelligence', true, 1, true, 'Z8jMd', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 10, 9, '#95B9C7', false, 69, 14, 'Research Affairs Coordinator', false, 1, false, '4znDd', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 10, 4, '#728FCE', true, 37, 12, 'Research and Development Associate', true, 1, true, '4QNOe', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 20, 3, '#2B65EC', false, 88, 14, 'Research and Development Director', false, 2, false, 'AIDq2', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 10, 5, '#306EFF', true, 97, 11, 'Research and Development Engineer', true, 1, true, 'kgZlg', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 15, 7, '#157DEC', false, 91, 19, 'Research and Development Manager', false, 2, false, '5LmbP', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 20, 1, '#1589FF', true, 33, 7, 'Research and Development Staff Scientist', true, 2, true, 'EA5pY', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 14, 4, '#6495ED', false, 58, 20, 'Research and Development Supervisor', false, 1, false, 'jmxFY', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 15, 6, '#6698FF', true, 55, 16, 'Research and Development Technician', true, 1, true, 'fwqq2', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 12, 9, '#38ACEC', false, 73, 17, 'Research and Development Tester', false, 2, false, '5xNDU', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 15, 7, '#56A5EC', true, 77, 12, 'Research Assistant', true, 2, true, 'v10MW', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 15, 9, '#5CB3FF', false, 45, 13, 'Research Assistant', false, 2, false, 'yMixa', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 14, 10, '#3BB9FF', true, 30, 14, 'Research Assistant - Bioprocessing', true, 1, true, 'PCwZw', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 20, 10, '#79BAEC', false, 59, 13, 'Research Associate', false, 1, false, '0DnHr', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 8, '#82CAFA', true, 54, 13, 'Research Associate, Oncology Biomarker Development', true, 2, true, 'ncywc', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 17, 2, '#82CAFF', false, 97, 15, 'Research Engineer', false, 1, false, 'viJJ3', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 15, 2, '#A0CFEC', true, 27, 20, 'Research Technician', true, 1, true, 'qwBgB', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 14, 2, '#B7CEEC', false, 81, 19, 'Researcher', false, 1, false, 'VNLgU', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 16, 3, '#B4CFEC', true, 49, 12, 'Reservations Agent', true, 1, true, 'edmqh', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 14, 9, '#C2DFFF', false, 52, 12, 'Reservations Agent', false, 1, false, 'btst0', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 17, 1, '#C6DEFF', true, 90, 20, 'Reservoir Engineer', true, 2, true, 'hUtKi', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 17, 10, '#AFDCEC', false, 86, 18, 'Residential Appraiser', false, 2, false, 'iUpKy', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 20, 10, '#ADDFFF', true, 85, 20, 'Residential Real Estate Agent', true, 1, true, '74IuJ', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 16, 2, '#BDEDFF', false, 32, 5, 'Residential Real Estate Broker', false, 2, false, 'Wo4gb', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 12, 8, '#CFECEC', true, 36, 16, 'Resource Coordinator', true, 1, true, 'w0q0E', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 13, 5, '#E0FFFF', false, 86, 7, 'Resource Room Teacher', false, 1, false, '2kVXA', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 14, 3, '#EBF4FA', true, 23, 15, 'Restaurant Manager', true, 1, true, 'QfWCo', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 20, 5, '#F0F8FF', false, 72, 14, 'Retail Analyst', false, 2, false, '1CDJm', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 13, 1, '#F0FFFF', true, 65, 15, 'Retail Associate', true, 1, true, 'Ej2HX', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 17, 5, '#CCFFFF', false, 96, 18, 'Retail Sales Associate', false, 2, false, 'Gw186', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 10, '#93FFE8', true, 22, 15, 'Retail Sales Consultant', true, 2, true, 'DaEuh', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 19, 10, '#9AFEFF', false, 41, 12, 'RF Engineer', false, 1, false, 'MYGek', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 11, 9, '#7FFFD4', true, 56, 14, 'Rheumatology Clinical Coordinator', true, 1, true, '6frDV', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 15, 4, '#00FFFF', false, 72, 12, 'Risk Manager', false, 2, false, 'upy2A', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 14, 9, '#7DFDFE', true, 27, 7, 'Roadie', true, 1, true, '3wgBg', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 13, 2, '#57FEFF', false, 63, 17, 'Roofer', false, 1, false, 'bSu3C', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 17, 1, '#8EEBEC', true, 99, 18, 'Room Attendant', true, 1, true, 'Z2Uvc', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 18, 3, '#50EBEC', false, 31, 13, 'Room Service Manager', false, 2, false, 'ZS4gb', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 18, 1, '#4EE2EC', true, 53, 20, 'Room Service Worker', true, 1, true, 'Ws3MT', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 14, 6, '#81D8D0', false, 65, 14, 'Rotating Equipment Engineer', false, 1, false, 'RU37M', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 12, 10, '#92C7C7', true, 99, 11, 'Route Salesperson', true, 1, true, 'dyJ43', 2, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 19, 3, '#77BFC7', false, 74, 16, 'Safety Consultant', false, 1, false, 'RFqbY', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true,'column alias 1', true, true, 17, 4, '#78C7C7', true, 94, 7, 'Safety Engineer', true, 2, true, 'UgpfD', 2, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 16, 5, '#48CCCD', false, 65, 20, 'Safety Scientist, Immunology', false, 2, false, 'h2kd6', 2, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 14, 2, '#43C6DB', true, 39, 17, 'Sales Agent', true, 2, true, 'UlofF', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 15, 6, '#46C7C7', false, 46, 15, 'Sales Analyst', false, 1, false, '2OhJ3', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 16, 6, '#43BFC7', true, 27, 8, 'Sales and Marketing Coordinator', true, 2, true, 'zLbVe', 1, 1, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 15, 8, '#3EA99F', false, 22, 6, 'Sales Assistant', false, 1, false, 'aHoCQ', 1, 2, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 13, 3, '#3B9C9C', true, 65, 5, 'Sales Compensation Administration Manager', true, 2, true, 'mw6Sd', 1, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false,'column alias 1', false, false, 10, 10, '#438D80', false, 77, 10, 'Sales Consultant', false, 1, false, 'WwpUA', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 19, 4, '#348781', true, 28, 10, 'Sales Coordinator', true, 1, true, 'qgcXv', 1, 1, 2);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 11, 8, '#5E7D7E', true, 93, 15, 'Sales Engineer', true, 1, true, 'SvCAV', 2, 2, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), true, 'column alias 1',true, true, 14, 6, '#3D3C3A', true, 69, 6, 'Project Manager104 UPDATE', true, 2, true, 'sss', NULL, NULL, 1);
INSERT INTO "column_gdma2" VALUES (nextval('seq_column_gdma2'), false, 'column alias 1',false, false, 14, 9, '#463E3F', false, 29, 12, 'Project Support105 update', false, 2, false, 'sss', NULL, NULL, 2);




INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',5,10);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',2,2);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',1,8);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',4,3);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',4,6);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',2,9);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',1,2);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',6,3);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',4,2);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',6,1);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',1,10);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',5,8);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',4,4);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',6,7);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',2,8);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',6,8);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',2,6);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',6,9);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',1,3);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',5,1);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',3,6);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',3,10);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',6,10);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',1,1);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',5,4);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',1,4);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',3,7);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',6,4);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',3,2);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',4,8);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',3,4);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',6,2);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',2,10);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',1,7);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',1,9);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',4,10);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',5,5);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',4,1);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',4,7);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',6,5);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',5,2);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',4,9);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',1,5);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',5,6);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',3,5);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',2,7);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',4,5);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',3,9);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'true','true','true','true',3,8);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',3,1);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',2,5);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',6,6);
INSERT INTO "user_table_access_gdma2" (id,allow_delete,allow_display,allow_insert,allow_update,table_id,user_id ) VALUES (nextval('seq_user_table_access_gdma2'),'false','false','false','false',2,4);

=====

TEST URLS
  see *Resource files e.g. ServerResource.java and RequestMappings used
  
  	SERVER
  		http://localhost:8080/gdma2/server
  		http://localhost:8080/gdma2/rest/server/table
  	
  	TABLE
  		http://localhost:8080/gdma2/rest/table/server/1/table/list?order[0][column]=1&search[value]=a
  	
  	USER
  		http://localhost:8080/gdma2/rest/user/table
  		http://localhost:8080/gdma2/rest/user/table?search[value]=com
	
		Search 'on', length 20, order by lastname:	
			http://localhost:8080/gdma2/rest/user/table?search[value]=on&length=20&order[0][column]=2
	
  	COLUMS
  		http://localhost:8080/gdma2/rest/column/list
  	
  	For table '2':
  		SEARCH 'sales' order by name DESC	
  			http://localhost:8080/gdma2/rest/column/table/2?length=4&order[0][column]=1&order[0][dir]=desc&start=2&search[value]=sales
  	
  	USER ACCESS
  		For table '2':
  		http://localhost:8080/gdma2/rest/access/table/2?length=5&search[value]=emma
  		
===========



===============start testing table SYNCH=======================================


	****************************************
	TESTING TABLE SYNCH (used in Admin module)
	****************************************
	
open ADPR_TEST DB

	REGISTER NEW SERVER (id=5)

	POPULATE NEW SERVER INITIALLY WITH TABLES AND COLUMNS:
	http://localhost:8080/gdma2/rest/server/metadata/5

CHECK RESULT
	select * from table_gdma2 where server_id = 5;
	select count(*) from column_gdma2 where table_id in (
		select id from table_gdma2 where server_id = 5
      ) /*238*/

/***************************/	

TESTING SYNCH: 

  FOR TEST: 
	DEACTIVATE some table in GDMA from Initial LOAD
	DELETE SOME TABLE IN REMOTE ADPR DB
	ADD new TABLE in REMOTE DB
	
THEN RUN:
	http://localhost:8080/gdma2/rest/server/metadata/tablesynch/5

CHECK RESULT: 	
 select * from table_gdma2 where server_id = 5;

 select count(*) from column_gdma2 where table_id =378;

EXPECTED RESULT: 

Active Synched table list for serverId = 5;	
	(Try Postaman : http://localhost:8080/gdma2/rest/table/server/5/active )

Based on Synch Rules explained in detail in DynamicDAOImpl.resyncTableList() you should see :
 - OLD DB tables (if they where ACTIVE and still exist in REMOTE DB)
 - OLD DB tables with Timestamp suffix - if they where not Active , also new Rempte DB table name is added now to local
 - NEW DB tables if they they WHERE not present in local DB and menawhile WHERE created on Remote DB

 Hibernate will perform 2 tables INSERTs (2 new tables, no columns yet) and
 2 table Updates -  Existing Tables - deactivated and Name changed

	OPEN Q: How to see Hibenrate ? params in app log??
 	

REPEATING TEST: 
  DELETE ALL ENTRIES ON SERVER to start TEST again

	/*DELETE COLUMNS for all tables for server 5*/
	delete from column_gdma2 where table_id in (
		select id from table_gdma2 where server_id = 5
	)
	
	/*DELETE TABLES for server 5*/
	delete from table_gdma2 where server_id = 5;
	
	
===============end testing TableSync================

  		
  		
  		
/*****TESTING COLUMN RESYNCH******/

PRECONDITION: Table resynch is done

		current precondition URL (will be changed): 	
STEP 1	http://localhost:8080/gdma2/rest/server/metadata/5

---------

STEP 2: Remote tables are not on local, do followling changes before column resynch: 

Column resyng URL:
	URL: http://localhost:8080/gdma2/rest/server/metadata/columnsynch/server/5/table/490



	GDMA table					REMOTE table
	-----------------------------------------------
1.	A (active)							A(exists)
	Update A:	.setColumnType
			setColTypeStr
			Nullable
			ColSize
			active

For test: after Table resynch, change values of 5 columns : setColumnType...to asure code from above is working 
	
2. B(not active) 							B(exists)
 
 For test: after Table resynch, Deactivate B
 
 a) Update B: 	
 		  add Timestamp to name
 		  deactivate
 
 b) Create new B using Remote B from metadata 		  

3.  Non-existing				C (New table)

For test: after Table resynch, create new Column C on remote DB	

	Create C using remote table

4.  D (still exists on local)		Deleted D on Remote
	
For test: after Table resynch, Delete D on remote
	
	Update D: 
		setDisplayable(false)
		setActive(false)

	Set all D's FK dependencies in all tables on server to null for DropDownColumnDisplay and DropDownColumnStore

-------------	
		
	
STEP 3: 
Column Resynch: 
	http://localhost:8080/gdma2/rest/server/metadata/5	

-----------
 HELP SQL : 
 

	
 select * from column_gdma2 where table_id in (
		select id from table_gdma2 where server_id = 5
	)

 select * from column_gdma2 where table_id in (
		select id from table_gdma2 where server_id = 5 
		and UPPER(name) = UPPER('monday1556')
	) order by id ;

select id, active, name,allow_insert, allow_update, displayed, is_nullable, order_by, table_id, column_type,column_type_str, is_nullable, column_size from column_gdma2 where table_id in (
		select id from table_gdma2 where server_id = 5
			) and table_id = 490 order by id;

  		
	
===============end Column resynch==============  		