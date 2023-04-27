
INSERT INTO time_zone (time_zone) VALUES('GMT Greenwich Mean Time GMT');
INSERT INTO time_zone (time_zone) VALUES('UTC Universal Coordinated Time GMT');
INSERT INTO time_zone (time_zone) VALUES('ECT European Central Time GMT+1:00');
INSERT INTO time_zone (time_zone) VALUES('EET Eastern European Time GMT+2:00');
INSERT INTO time_zone (time_zone) VALUES('ART (Arabic) Egypt Standard Time GMT+2:00');
INSERT INTO time_zone (time_zone) VALUES('EAT Eastern African Time GMT+3:00');
INSERT INTO time_zone (time_zone) VALUES('MET Middle East Time GMT+3:30');
INSERT INTO time_zone (time_zone) VALUES('NET Near East Time GMT+4:00');
INSERT INTO time_zone (time_zone) VALUES('PLT Pakistan Lahore Time GMT+5:00');
INSERT INTO time_zone (time_zone) VALUES('IST India Standard Time GMT+5:30');
INSERT INTO time_zone (time_zone) VALUES('BST Bangladesh Standard Time GMT+6:00');
INSERT INTO time_zone (time_zone) VALUES('VST Vietnam Standard Time GMT+7:00');
INSERT INTO time_zone (time_zone) VALUES('CTT China Taiwan Time GMT+8:00');
INSERT INTO time_zone (time_zone) VALUES('JST Japan Standard Time GMT+9:00');
INSERT INTO time_zone (time_zone) VALUES('ACT Australia Central Time GMT+9:30');
INSERT INTO time_zone (time_zone) VALUES('AET Australia Eastern Time GMT+10:00');
INSERT INTO time_zone (time_zone) VALUES('SST Solomon Standard Time GMT+11:00');
INSERT INTO time_zone (time_zone) VALUES('NST New Zealand Standard Time GMT+12:00');
INSERT INTO time_zone (time_zone) VALUES('MIT Midway Islands Time GMT-11:00');
INSERT INTO time_zone (time_zone) VALUES('HST Hawaii Standard Time GMT-10:00');
INSERT INTO time_zone (time_zone) VALUES('AST Alaska Standard Time GMT-9:00');
INSERT INTO time_zone (time_zone) VALUES('PST Pacific Standard Time GMT-8:00');
INSERT INTO time_zone (time_zone) VALUES('PNT Phoenix Standard Time GMT-7:00');
INSERT INTO time_zone (time_zone) VALUES('MST Mountain Standard Time GMT-7:00');
INSERT INTO time_zone (time_zone) VALUES('CST Central Standard Time GMT-6:00');
INSERT INTO time_zone (time_zone) VALUES('EST Eastern Standard Time GMT-5:00');
INSERT INTO time_zone (time_zone) VALUES('IET Indiana Eastern Standard Time GMT-5:00');
INSERT INTO time_zone (time_zone) VALUES('PRT Puerto Rico and US Virgin Islands Time GMT-4:00');
INSERT INTO time_zone (time_zone) VALUES('CNT Canada Newfoundland Time GMT-3:30');
INSERT INTO time_zone (time_zone) VALUES('AGT Argentina Standard Time GMT-3:00');
INSERT INTO time_zone (time_zone) VALUES('BET Brazil Eastern Time GMT-3:00');
INSERT INTO time_zone (time_zone) VALUES('CAT Central African Time GMT-1:00');

INSERT INTO country (name,time_zone_id) VALUES('China',1);
INSERT INTO country (name,time_zone_id) VALUES('India',2);
INSERT INTO country (name,time_zone_id) VALUES('United States',3);
INSERT INTO country (name,time_zone_id) VALUES('Indonesia',4);
INSERT INTO country (name,time_zone_id) VALUES('Pakistan',5);
INSERT INTO country (name,time_zone_id) VALUES('Brazil',6);
INSERT INTO country (name,time_zone_id) VALUES('Nigeria',7);
INSERT INTO country (name,time_zone_id) VALUES('Bangladesh',8);
INSERT INTO country (name,time_zone_id) VALUES('Russia',9);
INSERT INTO country (name,time_zone_id) VALUES('Mexico',10);
INSERT INTO country (name,time_zone_id) VALUES('Japan',11);

INSERT INTO user_center_detail (role) VALUES('SUPER_ADMIN');
INSERT INTO user_center_detail (role) VALUES('SUPER_ADMIN');

INSERT INTO user_details (first_name,last_name,email,username,password,center_detail) VALUES('puneet','bholewasi','puneet@gmail.com','puneet','pass@1234',1);
INSERT INTO user_details (first_name,last_name,email,username,password,center_detail) VALUES('deepak','grover','deepak@gmail.com','deepak','pass@1234',2);