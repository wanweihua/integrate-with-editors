Integration with ONLYOFFICE Document Server
===========================================

Simple application integrated with ONLYOFFICE
[Document Server](https://github.com/ONLYOFFICE/DocumentServer).

Build
-----

JDK >= 1.8 required. Build an executable jar:

    $ ./mvnw clean package

Run
---

Start document server with `docker-compose`:

    $ cd editors
    $ docker-compose up -d

Edit application configuration in file `application.yml`
(you can use `application.sample.yml` as example).

Start as Spring Boot application:

    $ ./mvnw spring-boot:run

Or run compiled version:

    $ java -jar target/integrate-with-editors.jar

License
-------

BSD, see `LICENSE`.
