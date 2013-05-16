
CREATE TABLE `region` (
  id tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  region varchar(5) NOT NULL,
  iso3166_1 char(2) NOT NULL,
  iso3166_2 varchar(5) NOT NULL,
  sort tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE `rank` (
  id tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  rank varchar(20) NOT NULL,
  sort tinyint(3) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE `habit` (
  id tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  habit varchar(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE `status` (
  id tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  status varchar(20),
  PRIMARY KEY (id)
);

CREATE TABLE `reference` (
  id integer(5) unsigned NOT NULL AUTO_INCREMENT,
  referencecode varchar(255) NOT NULL,
  referenceshort varchar(255) NOT NULL,
  `reference` TEXT NOT NULL,
  url varchar(255) NOT NULL,
  cdate datetime NOT NULL,
  mdate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);