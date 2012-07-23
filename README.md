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
* [Canadensys Core 1.0](https://github.com/Canadensys/canadensys-core)

* H2 Database 1.3.163 (for unit testing only)

Build
-----
To build a jar file:
```
mvn package
```
To install it in your local repository:
```
mvn install
```

Tests
-----
Unit tests
```
mvn test
```