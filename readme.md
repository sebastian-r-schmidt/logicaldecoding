# Parsing PostgreSQL Logical Decoding Output

Logical Deocding introduced with PostgreSQL 9.4 makes it possible to keep track of all commits into your database in commit order. This opens possibilities for auditing and other cool stuff.

No triggers needed.

No changes needed in your applications writing data.

This software is targeted for PostGIS users and can do two things:

1. Writing an audit log back into the database. The audit log will be in a table containing all values before the change and after the change (both in a JSONB column), timestamp of the commit and a geometry column describing the Bounding box of the affected region.
2. Publishing changes into a GeoWebCache instance triggering Seed, Reseed, or Truncate operations in the region the change has happened.

###Prerequisites
+ PostgreSQL 9.4 or higher
+ Java 8 JRE (tested with Oracle JRE)

####PostgreSQL Configuration
1. In your postgresql.conf set wal_level to logical
2. In your postgresql.conf set max_replication_slots to > 1
3. Set the tables you want to audit to replica identity full
```
ALTER TABLE schema.tablename REPLICA IDENTITY FULL;
```
4. create a replication slot:
```
SELECT * FROM pg_create_logical_replication_slot('repslot_test', 'test_decoding');
```

###Usage
```
git clone https://github.com/sebastian-r-schmidt/logicaldecoding
mvn clean package
cd target
java -jar logicaldecoding-<version>.jar
```

####Configuration
Create a application.properties file in the same folder where you placed the jarfile containing all settings.

Those values will override the .properties file packaged inside the jar file.

See src/main/resources/application.properties for examples and default values.

###Cleanup
Make sure you drop your replication slot when you do not need it anymore and no consumer is attached to it.
Otherwise your PostgreSQL instance will keep all WAL files forver inside pg_xlog directory
and bloat your harddisk:
```
SELECT * FROM pg_drop_replication_slot('repslot_test');
```

Sample SQL Statements for management of replication slots can be found in the
src/main/resources directory. 

###Used Libraries

+ Spring Boot
+ ANTLR v4.5 for parsing
+ Google Guava
+ Jackson for JSON generation
+ JTS for PostGIS Geometry processing

####License

This software is distributed under the MIT License. See LICENSE file for details.