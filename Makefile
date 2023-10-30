help: ## Show this help
	@egrep -h '\s##\s' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

runApplicationWithDb: ## Run application with all dependencies
	./mvnw compile jib:dockerBuild
	docker compose up

test: ## Execute the tests
	./mvnw clean test

testAPI: ## Run one example to check API is working (please deploy using runApplicationWithDb)
	 curl --location 'localhost:8080/api/v1/users-details' --header 'Content-Type: application/json' --data '{"email" : "curltest@curltest.test","name": "Curl Test Name", "surname" : "Curl Test Surname"}'

cleanUpDockerVolumes:   ## WARNING! This will clean all docker volumes!
	 docker volume rm $(docker volume ls -q)

