REST requests using curl:
-------------------------
- delete user with id=13:
curl -X DELETE http://localhost:8080/touch-web/resources/users/13

- add user
curl -d "name=name&pw=pass" http://localhost:8080/touch-web/resources/users

Create a database for running the application at $HOME/.touchdb/prod
--------------------------------------------------------------------
mvn sql:execute
 
To run hsqldb client:
---------------------
 mvn exec:java -Dexec.mainClass="org.hsqldb.util.DatabaseManager"