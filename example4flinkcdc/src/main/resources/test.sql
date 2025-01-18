
-- 记得开启test和test_route两个数据库的binlog, 并设置binlog_format=ROW
create database test;
create database test_route;

use test;
CREATE TABLE t1
(
    `id`   VARCHAR(255) PRIMARY KEY,
    `name` VARCHAR(255)
);
CREATE TABLE t2
(
    `id`   VARCHAR(255) PRIMARY KEY,
    `name` VARCHAR(255)
);
CREATE TABLE t3
(
    `id`  VARCHAR(255) PRIMARY KEY,
    `sex` VARCHAR(255)
);
use test;
INSERT INTO t1 VALUES('1001','zhangsan');
INSERT INTO t1 VALUES('1002','lisi');
INSERT INTO t1 VALUES('1003','wangwu');

INSERT INTO t2 VALUES('1001','zhangsan');
INSERT INTO t2 VALUES('1002','lisi');
INSERT INTO t2 VALUES('1003','wangwu');

INSERT INTO t3 VALUES('1001','F');
INSERT INTO t3 VALUES('1002','F');
INSERT INTO t3 VALUES('1003','M');


use test_route;

CREATE TABLE t1
(
    `id`   VARCHAR(255) PRIMARY KEY,
    `name` VARCHAR(255)
);
CREATE TABLE t2
(
    `id`   VARCHAR(255) PRIMARY KEY,
    `name` VARCHAR(255)
);
CREATE TABLE t3
(
    `id`  VARCHAR(255) PRIMARY KEY,
    `sex` VARCHAR(255)
);

use test_route;
INSERT INTO t1 VALUES('1001','zhangsan');
INSERT INTO t1 VALUES('1002','lisi');
INSERT INTO t1 VALUES('1003','wangwu');

INSERT INTO t2 VALUES('1004','zhangsan');
INSERT INTO t2 VALUES('1005','lisi');
INSERT INTO t2 VALUES('1006','wangwu');

INSERT INTO t3 VALUES('1001','F');
INSERT INTO t3 VALUES('1002','F');
INSERT INTO t3 VALUES('1003','M');

