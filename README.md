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

1. Copy `src/main/resources/db.properties.template` to
`src/main/resources/db.properties` and update the file with your database
connections settings.


2. Run the following SQLs to create tables in the appropriate databases:

    /src/main/resources/db.sql  <= creates table for storing users, roles and permissions
    /src/main/resources/datalogs.sql <= creates tables for storing logs
	/src/main/resources/cube.sql <= creates tables for storing the cube model for the datalogger

3. Call /setup (e.g. http://localhost:8080/test/setup) in order to create default users 
   and the profiles tables.

4. Copy the ilearnrw/data dir to the server.

Deployment
==========

In `WEB-INF/classes/apps.properties` change the api.baseurl property.
In `WEB-INF/classes/api.properties` change the api.baseurl property;
In `src/main/java/com/ilearnrw/api/info/InfoController` increase the returned value of the method getVersion() 

Symlink the data directory from the ilearnrw project into the deployment.

