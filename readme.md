# Parsing PostgreSQL Logical Decoding Output

Logical Deocding introduced with PostgreSQL 9.4 makes it possible to keep track of all commits into your database in commit order. This opens possibilities for auditing and other cool stuff.

No triggers needed.
No Changes needed in your applications writing data.

This software is targeted for PostGIS users and can do two things:
1. writing an audit log back into the database, including the region where changes have happened.
2. publishing changes into a GeoWebCache instance triggering Seed,Reseed, or truncate operations in the region the change has happend.

###Prerequisites
PostgreSQL 9.4 or higher

####PostgreSQL Configuration
in your postgresql.conf set wal_level to logical
in your Postgresql.conf set max_replication_slots to > 1

create a replication slot:
SELECT * FROM pg_create_logical_replication_slot('repslot_test', 'test_decoding');

###Usage
```
git clone https://github.com/sebastian-r-schmidt/logicaldecoding
mvn clean package
cd target
java -jar logicaldecoding-<version>.jar
```

####Configuration
Create a application.properties file inside your jar directory containing all settings.
Those values will override the .properties file packaged inside the jar file.
see src/main/resources/application.properties for examples.

###Used Libraries
Spring Boot
ANTLR v4 for parsing