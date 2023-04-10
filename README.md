# Real estate Backend server

## Install and run the server

You must have access to a PostgresSQL database

Create a Jks key file: using `keytool` command (provided in jdks) like this :  
  `keytool -genkey -keyalg RSA -keysize 4096 -alias [alias_name] -keystore [filename].jks`

- Duplicate the `src/main/resources/application.properties.TEMPLATE` file in the same folder
- Rename this new file `application.properties`
- Change the fields between brackets with appropriate values
- Use the command `mvn clean compile` in order to generate the source files
- Launch the server with the `src/main/java/com/github/m2cyurealestate/real_estate_back/RealEstateBackApplication.java` file