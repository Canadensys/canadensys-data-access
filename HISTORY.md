Version History
===============
Version 2.5.0 2014-04-03
* Added 'parentid' to Vascan lookup nested sets
* Some optimizations in HibernateTaxonDAO

Version 2.4.0 2014-03-27
* OccurrenceDAO: added sorting and paging support for searchWithLimit
* Added ImportLogModel handling
* Database Schema changes: [2.3.0_to_2.4.0.sql](https://github.com/Canadensys/canadensys-data-access/blob/dev/script/migrations/occurrence/2.3.0_to_2.4.0.sql)
* Now using Spring 4.0.2 and Hibernate 4.3.2

Version 2.3.0 2014-01-31
* Database Schema changes: [2.2.2_to_2.3.0.sql](https://github.com/Canadensys/canadensys-data-access/blob/dev/script/migrations/occurrence/2.2.2_to_2.3.0.sql)
* Refactor ResourceContactModel
* OccurrenceDAO: getOccurrenceSummaryJson is now deprecated, replaced by loadOccurrenceSummary
* Now using Spring 3.2.6 and Hibernate 4.2.8

Version 2.2.2 2014-01-24
* Update dependencies
* Added associatedMediaMime

Version 2.2.1 2013-12-04
* Reorganize scripts

Version 2.2.0 2013-10-16
* Add DarwinCoreTermUtils
* Update dependencies (Spring, Jackson, canadensys-core)

Version 2.1.1 2013-10-03
* Bug fix: HibernateTaxonDAO.getStatusRegionCriterion(...) String[] region parameter should not be case sensitive.

Version 2.1.0 2013-09-27
* ElasticSearchNameDAO search function can search with or without autocompletion.
* ElasticSearchNameDAO search function now includes epithet and "genus first letter".

Version 2.0.1 2013-08-23
* Add support for synonym with more than one parent in NameDAO and model

Version 2.0 2013-08-14
* No more GeneratedValue on OccurrenceRawModel
* New Vascan package (dao and model)
* Now using Spring 3.2 and Hibernate 4.1
* New ResourceContact model and DAO
* Added migration scripts folder
