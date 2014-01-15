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


Copy the ilearnrw/data dir to the server.



Create default users
====================

Default users will be imported into the user service in the db.sql.

This will not create the profiles, easiest way to get them created is to login
as admin and view the profile for each user once (selecting "View Profile" in
the "Users" table):

The panel interface uses the user-id as user id in the profile service. The datalogger uses
the username as user identifier.


Deployment
==========

In `WEB-INF/classes/apps.properties` change the api.baseurl property.
In `WEB-INF/classes/api.properties` change the api.baseurl property;

Symlink the data directory from the ilearnrw project into the deployment.

