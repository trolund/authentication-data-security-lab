# Printer server
This application has been developed for the course 02239 Data Security at Denmark's Technical University. 

To run the application
- Start the database in a docker container "docker-compose up -d"
  
- Run maven install to get all the dependency packages
- Start the server application.
  - Each time the server starts, it clears the database and creates the users listed in the /src/main/java/server/data/mocking/MockUserData.java
  - A test user all ready exists in the mocking class. (Username:john@doe.com password:doe)
- Start the client application and login.
