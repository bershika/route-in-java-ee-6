-- You can use this file to load seed data into the database using SQL statements
insert into MEMBER (name, email) values ('Anna', 'bershika@gmail.com')
insert into SURCHARGE (email, value) values ('bershika@gmail.com', 25)
insert into LOCATION(city,state) values ('Seattle', 'WA')
insert into LOCATION(city,state, lan, lng) values ('Kent', 'WA', 0, 0)
insert into HUB(city,state) values ('Seattle', 'WA')
insert into LOCATION(city,state, lan, lng) values ('Dallas', 'TX', 0, 0)
insert into HUB(city,state, a1, a2, b1, b2, manualMode) values ('Dallas', 'TX', 0, 0, 0, 0, false)
insert into MEMBER_HUBSERVICE(city,state, rate, email) values ('Dallas', 'TX', 1234, 'bershika@gmail.com')
insert into ROUTE(hubName,hubState, destName, destState, distance) values ('Seattle', 'WA', 'Kent', 'WA', 13)
insert into POINT(hubName,hubState, destName, destState, rate, fake) values ('Seattle', 'WA', 'Kent', 'WA', 23, false)

INSERT INTO `STATES` VALUES ('AL'),('AZ'),('AR'),('CA'),('CO'),('CT'),('DE'),('DC'),('FL'),('GA'),('ID'),('IL'),('IN'),('IA'),('KS'),('KY'),('LA'),('ME'),('MD'),('MA'),('MI'),('MN'),('MS'),('MO'),('MT'),('NE'),('NV'),('NH'),('NJ'),('NM'),('NY'),('NC'),('ND'),('OH'),('OK'),('OR'),('PA'),('RI'),('SC'),('SD'),('TN'),('TX'),('UT'),('VT'),('VA'),('WA'),('WV'),('WI'),('WY');


select * from SYNONYM;


select * from POINT where hubName like 'Charleston' and hubState like 'SC' and destName like 'Simpsonville' and destState like 'SC' and rate = 778.95;
select * from ROUTE where hubName like 'Charleston' and hubState like 'SC' and destName like 'Simpsonville' and destState like 'SC';
select * from ROUTE where hubName like 'Long Beach' and hubState like 'CA' and destName like 'Carmel' and destState like 'CA';