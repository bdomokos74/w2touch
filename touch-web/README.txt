To create a database:
mvn exec:java -Dexec.mainClass="org.hsqldb.Server" -Dexec.args="-database.0 file:runtime/db -dbname.0 touchdb-devel"

To run hsqldb client:
mvn exec:java -Dexec.mainClass="org.hsqldb.DatabaseManager" -Dexec.args="-database.0 file:runtime/db -dbname.0 touchdb-devel"