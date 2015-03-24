Version History
===============

Version 2.14.0 2015-03-24
* Maven script set to Java 7 target
* Changed loadTaxon metod signature
* [Code diff](https://github.com/Canadensys/canadensys-data-access/compare/2.13.1...2.14.0)

Version 2.13.1 2015-03-19
* Bug fix: [code diff](https://github.com/Canadensys/canadensys-data-access/compare/2.13.0...2.13.1)

Version 2.13.0 2015-03-19
* New denormalized queries in TaxonDAO
* New jooq dependency

Version 2.12.1 2015-01-27
* Fixed dwca-reader dependency

Version 2.12.0 2014-12-17
* Added searchIterator(...) to TaxonDAO, loadCompleteTaxonData is now deprecated.
* Some changes in TaxonModel (use Set instead of List, remove the @ManyToMany from getDistribution())
* Fixed an issue with ScrollableResultsIteratorWrapper 

Version 2.11.0 2014-10-01
* Updated support for occurrence extensions
* Database Schema changes: [2.10.0_to_2.11.0.sql](https://raw.githubusercontent.com/Canadensys/canadensys-data-access/dev/script/migrations/occurrence/2.10.0_to_2.11.0.sql)
* Added  bibliographiccitation and occurrenceid to OccurrenceModel
* Introduce resource_uuid to ResourceModel

Version 2.10.0 2014-08-05
* Added hasassociatedsequences to OccurrenceModel
* Database Schema changes: [2.9.0_to_2.10.0.sql](https://raw.githubusercontent.com/Canadensys/canadensys-data-access/dev/script/migrations/occurrence/2.9.0_to_2.10.0.sql)
* Update to ElasticSearch 0.90.12

Version 2.9.0 2014-07-30
* Possible breaking change: resource_management is now handled by the data-access layer which could conflit with previous harvester versions.
* Database Schema changes: [2.8.0_to_2.9.0.sql](https://raw.githubusercontent.com/Canadensys/canadensys-data-access/dev/script/migrations/occurrence/2.8.0_to_2.9.0.sql)
* Added ResourceModel and matching DAO
* Added support for DwC Extensions (OccurrenceExtensionModel/DAO)

Version 2.8.0 2014-07-14
* Database Schema changes: [2.7.0_to_2.8.0.sql](https://raw.githubusercontent.com/Canadensys/canadensys-data-access/dev/script/migrations/occurrence/2.7.0_to_2.8.0.sql)
* Added associatedsequences and basisofrecord, removed associatedmediamime(OccurrenceModel).

Version 2.7.1 2014-06-05
* Improve validation for geospatial query handling

Version 2.7.0 2014-05-29
* Database Schema changes: [2.6.0_to_2.7.0.sql](https://raw.githubusercontent.com/Canadensys/canadensys-data-access/dev/script/migrations/occurrence/previous/2.6.0_to_2.7.0.sql)
* Added hasTypeStatus
* Geospatial query support
* Add 'hints' to SearchQueryPart

Version 2.6.0 2014-04-14
* Minor improvements in TaxonDAO introducing new method signatures.

Version 2.5.0 2014-04-03
* Added 'parentid' to Vascan lookup nested sets
* Some optimizations in HibernateTaxonDAO

Version 2.4.0 2014-03-27
* OccurrenceDAO: added sorting and paging support for searchWithLimit
* Added ImportLogModel handling
* Database Schema changes: [2.3.0_to_2.4.0.sql](https://raw.githubusercontent.com/Canadensys/canadensys-data-access/dev/script/migrations/occurrence/previous/2.3.0_to_2.4.0.sql)
* Now using Spring 4.0.2 and Hibernate 4.3.2

Version 2.3.0 2014-01-31
* Database Schema changes: [2.2.2_to_2.3.0.sql](https://github.com/Canadensys/canadensys-data-access/blob/dev/script/migrations/occurrence/previous/2.2.2_to_2.3.0.sql)
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
