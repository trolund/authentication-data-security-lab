# Printer server

This application has been developed for the course 02239 Data Security at Denmark's Technical University.

To run the application

- Start the database in a docker container "docker-compose up -d"

- Run maven install to get all the dependency packages
- Start the server application.
    - Each time the server starts, it clears the database and creates the users listed in the
      /src/main/java/server/data/mocking/MockUserData.java
    - A test user all ready exists in the mocking class. (Username:john@doe.com password:doe)
- Start the client application and login.

# Auth Policy

There can be chosen between an ACL and RBAC authentication service. This service is loaded on printer server start and restart, thus you can modify the policy file while the server is running and apply the changes with a restart.

The policy can be chosen on application start, by given speicyfing the policy as the first argument to the program: 
* `acl` -> ACL
* `rbac` -> RBAC

# Users
You can use the following user to log into the system, they have the roles defined in the assignment.

| UserId  | FirstName | LastName | Password    |
| ------- | --------- | -------- | ----------- |
| alice   | Alice     | Doe      | Password123 |
| bob     | Bob       | Doe      | Password123 |
| cecilia | Cecilia   | Doe      | Password123 |
| david   | David     | Doe      | Password123 |
| erica   | Erica     | Doe      | Password123 |
| fred    | Fred      | Doe      | Password123 |
| george  | George    | Doe      | Password123 |