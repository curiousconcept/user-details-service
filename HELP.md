# Read Me First
To run this project and succeed its tests you need to have docker environment setup:

To solve: ["Docker environment isn't found"](https://stackoverflow.com/questions/61108655/test-container-test-cases-are-failing-due-to-could-not-find-a-valid-docker-envi)

Please use "Makefile" file within root dir to execute(or consult on) various commands against the project:
* build all docker images and run the project
* run all project tests
* run a test API call after service is ran

### Project description
The service provides an API to save user details securely. 
It allows to create new user details and fetch them by id. 
The user details are encrypted and saved as BLOB inside the MySQL database. 
Encryption is using AES symmetric encryption.

### Tests
Test use embedded container for persistence level testing as well as integration testing.
This setup requires docker to run.