canadensys-data-access
======================

Canandensys data access layer containing DAO and models.

Dependencies
------------
* [Apache Maven 3](http://maven.apache.org/)
* [Spring Framework 3.2](http://www.springsource.org/spring-framework)
* [Hibernate 4.1](http://www.hibernate.org/)
* [Apache Commons BeanUtils 1.8.3](http://commons.apache.org/beanutils/)
* [Jackson Mapper 1.9.12](http://jackson.codehaus.org/)
* [GBIF DarwinCoreArchiveReader 1.9.1](http://code.google.com/p/darwincore/wiki/DarwinCoreArchiveReader)
* [JSON.org](http://www.json.org/java/)
* [Canadensys Core 1.5](https://github.com/Canadensys/canadensys-core)

Optional
* [Elastic Search 0.90.2](http://www.elasticsearch.org/)

Testing only
* [H2 Database 1.3.172](http://www.h2database.com)

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