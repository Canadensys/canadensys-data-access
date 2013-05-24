INSERT INTO `status` (id,status) VALUES (1,'accepted'),(2,'synonym');

INSERT INTO `habit` (id,habit) VALUES (1,''),(2,'herb'),(3,'shrub'),(4,'tree'),(5,'vine');

INSERT INTO `excludedcode` (id,excludedcode) VALUES 
(1,''),
(2,'misidentification'),
(3,'documented'),
(4,'cultivated'),
(5,'old'),
(6,'ephemeral'),
(7,'determined');

INSERT INTO `distributionstatus` (id,distributionstatus) VALUES 
(1,'native'),
(2,'introduced'),
(3,'ephemeral'),
(4,'extirpated'),
(5,'excluded'),
(6,'doubtful'),
(7,'absent');

INSERT INTO `rank` (id,rank,sort) VALUES
(1,'Class',1),
(2,'Subclass',2),
(3,'Superorder',3),
(4,'Order',4),
(5,'Family',5),
(6,'Subfamily',6),
(7,'Tribe',7),
(8,'Subtribe',8),
(9,'Genus',9),
(10,'Subgenus',10),
(11,'Section',11),
(12,'Subsection',12),
(13,'Series',13),
(14,'Species',14),
(15,'Subspecies',15),
(16,'Variety',16),
(17,'',17);

INSERT INTO `region` (id,region,iso3166_1,iso3166_2,sort) VALUES
(1,'AB','CA','CA-AB',2),
(2,'BC','CA','CA-BC',1),
(3,'GL','GL','',16),
(4,'NL_L','CA','',11),
(5,'MB','CA','CA-MB',4),
(6,'NB','CA','CA-NB',7),
(7,'NL_N','CA','',10),
(8,'NT','CA','CA-NT',14),
(9,'NS','CA','CA-NS',9),
(10,'NU','CA','CA-NU',15),
(11,'ON','CA','CA-ON',5),
(12,'PE','CA','CA-PE',8),
(13,'QC','CA','CA-QC',6),
(14,'PM','FR','FR-PM',12),
(15,'SK','CA','CA-SK',3),
(16,'YT','CA','CA-YT',13);

INSERT INTO `reference` (id,referencecode) VALUES (1,'empty');
INSERT INTO `reference` (id,referencecode,referenceshort,reference,url) VALUES 
(105,'Chase&Reveal09','Chase & Reveal, 2009','Chase, M.W. & J.L. Reveal. 2009. A phylogenetic classification of land plants to accompany APG III. Botanical Journal of the Linnaen Society 161 (2): 122-127.','http://dx.doi.org/10.1111/j.1095-8339.2009.01002.x');

INSERT INTO taxon (id,uninomial,author,statusid,rankid,referenceid) VALUES
(73,'Equisetopsida','C. Aghard',1,1,105),
(26,'Equisetidae','Warming',1,2,105),
(33,'Polypodiidae','Cronquist, Takhtajan & Zimmermann',1,2,105);
		
INSERT INTO taxonomy (parentid,childid) VALUES (73,26);
INSERT INTO lookup  (taxonid,calname,calnameauthor,calnamehtml,calnamehtmlauthor,status,calhabit,ab,bc,gl,nl_l,mb,nb,nl_n,nt,ns,nu,`ON`,pe,qc,pm,sk,yt) VALUES
(73,'Equisetopsida','Equisetopsida C. Aghard','<em>Equisetopsida</em>','<em>Equisetopsida</em> C. Aghard',
'accepted','herb,shrub,tree,vine','native','native','native','native','native','native','native','native','native','native','native','native','native','native','native','native');

INSERT INTO vernacularname (id,name,statusid,taxonid,language,referenceid) VALUES
(1,'Fougères',1,33,'fr',1);
