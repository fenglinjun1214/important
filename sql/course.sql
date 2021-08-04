CREATE TABLE course_1 (
	cid BIGINT(20) PRIMARY KEY,
	cname VARCHAR(50) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	cstatus varchar(10) NOT NULL
);

CREATE TABLE course_2 (
	cid BIGINT(20) PRIMARY KEY,
	cname VARCHAR(50) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	cstatus varchar(10) NOT NULL
);

insert into course(cname,user_id,cstatus) values ('shardingProxy',100,'1');