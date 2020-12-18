Download keycloak 10.0.2 from https://downloads.jboss.org/keycloak/10.0.2/keycloak-10.0.2.zip
Change keycloak port to 9090 (go to ${keycloakfolder}/standalone/configuration and edit standalone xml, find "socket-binding name="http" port="${jboss.http.port:8080}"" and replace the port with 9090)
Unzip the data.zip located in the keycloak-data-import folder and copy the contents to ${keycloakfolder}/standalone
Start the keycloak server by running standalone.bat or standalone.sh in ${keycloakfolder}/bin.

Create a new mongodb at port 27017 and a with a db named "testssi", or run on docker with the commands:
docker pull mongo
docker run -d -p 27017:27017 --name mongodb mongo
and create a db named "testssi"

Do mvn clean install or mvn clean package for the application and run it with mvn spring-boot:run.
