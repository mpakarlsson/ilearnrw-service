ilearnrw-service
================

Development environment
=======================

List of requirements:

    * Spring MVC3
    * Database server

Server configuration
======================
HTTPS must be activated. This can be done by following the tutorial here: http://java.dzone.com/articles/setting-ssl-tomcat-5-minutes
The changes must be done on developer machines from STS, Package Explorer/Servers/VMware vFabric tc Server Developer Edition v2.9-config/server.xml, otherwise changes will be overwritten.

Properties files:
api.properties & apps.properties
auth.baseurl = the URL at which the Authentication API is available, default https://localhost:8443/test/
logs.baseurl = the URL at which the DataLogger API is available, default https://localhost:8443/test/

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

