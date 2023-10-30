### Requirements
To run this project and its tests you need to have docker environment setup.

To help with the most common docker issue: ["Docker environment isn't found"](https://stackoverflow.com/questions/61108655/test-container-test-cases-are-failing-due-to-could-not-find-a-valid-docker-envi).

Please check first two comments for MacOS and Ubuntu respectively(on ubuntu, don't forget "chmod" mentioned in the comment).

### Building & Running the project
Please use "Makefile" file within root dir(type "make" and then the task e.g. "make tests") to execute(or consult) various commands against the project:
* runApplicationWithDb - build all docker images and run the project (docker compose for compose.yaml + jib docker build maven plugin)
* tests - run all project tests (maven)
* testAPI - run a test API call after service is ran (curl)

### Inspecting the database

mysql -h localhost -P 3306 --protocol=tcp -u root -pasfgASFA42
USE main;
SELECT * FROM encrypted_user_details;

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

### Extra tasks
1. Spring JPA is using prepared statements against malicious SQL injection input, apache commons serializer recent version addressed remote code invocation.
2. GET as well as POST decrypts returning information
3. Containerized and DB dependency provided as requested. Application waits for database to be ready.
