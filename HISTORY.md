Version History
===============
Version 2.1.1 2013-20-03
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
