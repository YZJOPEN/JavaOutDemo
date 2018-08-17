DROP TABLE IF EXISTS t_outrecord;
CREATE TABLE t_outrecord (
    recordId varchar(125) NOT NULL,
    userName varchar(255) DEFAULT NULL,
    contact varchar(255) DEFAULT NULL,
    outTime varchar(125) NOT NULL,
    backTime varchar(125) NOT NULL ,
    outReason varchar(255) NOT NULL,
    photoUrl varchar(125) NOT NULL,
    deptName varchar(255) NOT NULL,
    state int(11) DEFAULT '1',
    orgId varchar(125) NOT NULL,
    openId varchar(125) NOT NULL,
    stage int(11),PRIMARY KEY (recordId));

DROP TABLE IF EXISTS t_clockin;
CREATE TABLE t_clockin (clockInId varchar(125) NOT NULL,
position varchar(255) DEFAULT NULL,
clockInTime varchar(125) NOT NULL,
state int(11) DEFAULT '1',
record_id varchar(125) DEFAULT NULL,PRIMARY KEY (clockInId));