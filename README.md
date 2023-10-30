### Requirements
To run this project and its tests you need to have docker environment setup:

To help with the most common docker issue: ["Docker environment isn't found"](https://stackoverflow.com/questions/61108655/test-container-test-cases-are-failing-due-to-could-not-find-a-valid-docker-envi)
Please check first two comments for MacOS and Ubuntu respectively(on ubuntu, don't forget "chmod" mentioned in the comment).

### Building & Running the project
Please use "Makefile" file within root dir to execute(or consult on) various commands against the project:
* build all docker images and run the project (docker compose for compose.yaml + jib docker build maven plugin)
* run all project tests (maven)
* run a test API call after service is ran (curl)

### Project description
The service provides an API to save user details securely. 
It allows to create new user details and fetch them by id. 
The user details are encrypted and saved as BLOB inside the MySQL database. 
Encryption is using AES symmetric encryption. A single password is used to encrypt 
all the user details, entry specific IV is added for extra security so that similar passwords don't 
expose the cipher.

### Tests
Tests use embedded container for persistence level testing as well as integration testing.

### Task objectives checklist
1. Data is encrypted using AES and stored in MySQL database
2. Unit Tests
3. Code commented
4. Project is built using Maven and Jib for containerization
5. Project is hosted on GitHub
6. README