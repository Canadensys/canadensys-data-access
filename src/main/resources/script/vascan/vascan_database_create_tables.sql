
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

CREATE TABLE `distribution` (
  `id` smallint(6) unsigned NOT NULL AUTO_INCREMENT,
  `taxonid` smallint(5) unsigned NOT NULL,
  `regionid` tinyint(3) unsigned NOT NULL,
  `distributionstatusid` tinyint(3) unsigned NOT NULL,
  `excludedcodeid` tinyint(3) unsigned NOT NULL,
  `referenceid` smallint(5) unsigned NOT NULL,
  `cdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `mdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `distribution_provinceid` (`regionid`),
  KEY `distribution_excludedcodeid` (`excludedcodeid`),
  KEY `distribution_statusid` (`distributionstatusid`),
  KEY `distribution_taxonid` (`taxonid`),
  KEY `distribution_referenceid` (`referenceid`)
);

CREATE TABLE `distributionstatus` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `distributionstatus` varchar(20) NOT NULL,
  `occurrencestatus` varchar(20) NOT NULL,
  `establishmentmeans` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `excludedcode` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `excludedcode` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
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

CREATE TABLE `lookup` (
  `calname` varchar(255) NOT NULL,
  `calnameauthor` varchar(255) NOT NULL,
  `calnamehtml` varchar(255) NOT NULL,
  `calnamehtmlauthor` varchar(255) NOT NULL,
  `calnameauthornoautonym` varchar(255)  DEFAULT NULL,
  `taxonid` smallint(5) NOT NULL,
  `isleaf` tinyint(1) NOT NULL DEFAULT '0',
  `status` varchar(20) NOT NULL,
  `rank` varchar(20) NOT NULL,
  `calhabit` varchar(25) DEFAULT NULL,
  `AB` varchar(20) DEFAULT NULL,
  `BC` varchar(20) DEFAULT NULL,
  `GL` varchar(20) DEFAULT NULL,
  `NL_L` varchar(20) DEFAULT NULL,
  `MB` varchar(20) DEFAULT NULL,
  `NB` varchar(20) DEFAULT NULL,
  `NL_N` varchar(20) DEFAULT NULL,
  `NT` varchar(20) DEFAULT NULL,
  `NS` varchar(20) DEFAULT NULL,
  `NU` varchar(20) DEFAULT NULL,
  `ON` varchar(20) DEFAULT NULL,
  `PE` varchar(20) DEFAULT NULL,
  `QC` varchar(20) DEFAULT NULL,
  `PM` varchar(20) DEFAULT NULL,
  `SK` varchar(20) DEFAULT NULL,
  `YT` varchar(20) DEFAULT NULL,
  `higherclassification` varchar(255) DEFAULT NULL,
  `class` varchar(255) DEFAULT NULL,
  `_order` varchar(255) DEFAULT NULL,
  `family` varchar(255) DEFAULT NULL,
  `genus` varchar(255) DEFAULT NULL,
  `subgenus` varchar(255) DEFAULT NULL,
  `specificepithet` varchar(255) DEFAULT NULL,
  `infraspecificepithet` varchar(255) DEFAULT NULL,
  `author` varchar(255)  DEFAULT NULL,
  `vernacularfr` varchar(255)  DEFAULT NULL,
  `vernacularen` varchar(255)  DEFAULT NULL,
  `_left` integer DEFAULT NULL,
  `_right` integer DEFAULT NULL,
  `parentid` integer DEFAULT NULL,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `p_AB` (`AB`,`BC`,`GL`,`NL_L`,`MB`,`NB`,`NL_N`,`NT`,`NS`,`NU`,`ON`,`PE`,`QC`,`PM`,`SK`,`YT`),
  KEY `taxonid` (`taxonid`)
);

CREATE TABLE `taxonomy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `childid` int(5) NOT NULL,
  `parentid` int(5) NOT NULL,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `acceptedid` (`childid`,`parentid`),
  KEY `childid` (`childid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT chk_circular_reference CHECK (childid <> parentid)
);

CREATE TABLE `taxonhybridparent` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `childid` smallint(5) NOT NULL,
  `parentid` smallint(5) NOT NULL,
  `sort` tinyint(1) NOT NULL DEFAULT '1',
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `taxonhabit` (
  `id` smallint(5) NOT NULL AUTO_INCREMENT,
  `taxonid` smallint(5) NOT NULL,
  `habitid` tinyint(3) unsigned NOT NULL,
  `sort` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
 );

CREATE TABLE `taxon` (
  `id` smallint(5) NOT NULL AUTO_INCREMENT,
  `uninomial` varchar(255) NOT NULL,
  `binomial` varchar(255) NOT NULL,
  `trinomial` varchar(255) NOT NULL,
  `quadrinomial` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `statusid` tinyint(3) DEFAULT NULL,
  `rankid` tinyint(3) DEFAULT NULL,
  `referenceid` smallint(5) unsigned NOT NULL,
  `commentary` text,
  `notaxon` smallint(5) unsigned NOT NULL DEFAULT '0',
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `vernacularname` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `statusid` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `taxonid` smallint(5) DEFAULT NULL,
  `language` char(2) DEFAULT NULL,
  `referenceid` smallint(5) NOT NULL,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `vernacularname_statusid` (`statusid`),
  KEY `vernacularname_taxonid` (`taxonid`)
);