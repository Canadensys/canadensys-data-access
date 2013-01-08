canadensys-data-access
======================

Canandensys data access layer containing DAO and models.

Dependencies
------------
* [Apache Maven 3](http://maven.apache.org/)
* [Spring Framework 3.1](http://www.springsource.org/spring-framework)
* [Hibernate 4.0](http://www.hibernate.org/)
* [Apache Commons BeanUtils 1.8.3](http://commons.apache.org/beanutils/)
* [Jackson Mapper 1.9.2](http://jackson.codehaus.org/)
* [GBIF DarwinCoreArchiveReader 1.9.1](http://code.google.com/p/darwincore/wiki/DarwinCoreArchiveReader)
* [JSON.org](http://www.json.org/java/)
* [Canadensys Core 1.3](https://github.com/Canadensys/canadensys-core)

* [H2 Database 1.3.163](http://www.h2database.com) (for unit testing only)

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