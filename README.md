canadensys-data-access
======================

Canandensys data access layer containing DAO and models.

Code Status
-----------
[![Build Status](https://travis-ci.org/Canadensys/canadensys-data-access.png)](https://travis-ci.org/Canadensys/canadensys-data-access)

Dependencies
------------
* [Apache Maven 3](http://maven.apache.org/)
* [Spring Framework](http://www.springsource.org/spring-framework)
* [Hibernate](http://www.hibernate.org/)
* [Apache Commons BeanUtils](http://commons.apache.org/beanutils/)
* [Jackson](http://wiki.fasterxml.com/JacksonHome)
* [GBIF dwca-reader](https://github.com/gbif/dwca-reader)
* [JSON.org](http://www.json.org/java/)
* [Canadensys Core](https://github.com/Canadensys/canadensys-core)

Optional
* [Elastic Search](http://www.elasticsearch.org/)

Testing only
* [H2 Database](http://www.h2database.com)

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