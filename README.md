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

There are 2 SQL files that you need to run:

    /src/main/resources/db.sql
    /src/main/resources/datalogger.sql

Note, The tables for storing user profiles has to be generated.
