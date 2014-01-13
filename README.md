ilearnrw-service
================

Development environment
=======================

List of requrements:

    * Spring MVC3
    * Database server

Database configuration
======================

Copy `src/main/resources/db.properties.template` to
`src/main/resources/db.properties` and update the file with your database
connections settings.

*Initial database import*

The following SQL files need to be run for a clean deployment:

    /src/main/resources/db.sql
    /src/main/resources/datalogs.sql
	/src/main/resources/cube.sql

Note, The tables for storing user profiles has to be generated using http://api.ilearnrw.eu/ilearnrw/profile/generateSql
