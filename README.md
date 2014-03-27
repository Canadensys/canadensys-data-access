canadensys-data-access
======================

Canandensys data access layer containing DAO and models.

Code Status
-----------
[![Build Status](https://travis-ci.org/Canadensys/canadensys-data-access.png)](https://travis-ci.org/Canadensys/canadensys-data-access)

Dependencies
------------
* [Apache Maven 3](http://maven.apache.org/)
* [Spring Framework 4.0.2.RELEASE](http://www.springsource.org/spring-framework)
* [Hibernate 4.3.2.Final](http://www.hibernate.org/)
* [Apache Commons BeanUtils 1.8.3](http://commons.apache.org/beanutils/)
* [Jackson 2.2.3](http://wiki.fasterxml.com/JacksonHome)
* [GBIF DarwinCoreArchiveReader 1.9.1](http://code.google.com/p/darwincore/wiki/DarwinCoreArchiveReader)
* [JSON.org](http://www.json.org/java/)
* [Canadensys Core 1.8](https://github.com/Canadensys/canadensys-core)

Optional
* [Elastic Search 0.90.2](http://www.elasticsearch.org/)

Testing only
* [H2 Database 1.3.175](http://www.h2database.com)

Build
-----
Build a jar file:
```
mvn package
```
Install to your local repository:
```
mvn install
```

Tests
-----
Unit tests
```
mvn test
```