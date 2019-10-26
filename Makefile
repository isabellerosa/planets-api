.PHONY: build dev prod stop clean stop-clean help

default: help

help:
	@echo ----------------------------------------------------------------------
	@echo "                      Available Commands"
	@echo ----------------------------------------------------------------------
	@echo "    > image - Builds the docker image"
	@echo "    > dev - Runs the api on dev mode on port 8081"
	@echo "    > prod - Runs the api on prod mode on port 8080."
	@echo "          Make sure to configure variables on planets-api.env file"
	@echo "    > stop - Stop the running containers"
	@echo "    > clean - Remove stopped containers"
	@echo "    > stop-clean - Stop and then remove containers"
	@echo "    > rmi - Remove image"

image:
	docker image build -t isabellerosa/planets-api .

dev:
	docker container run -d -p 8081:8080 isabellerosa/planets-api

prod:
	docker-compose -f docker-compose.yml up -d

stop:
	docker container stop $(shell docker container ls -q --filter ancestor=isabellerosa/planets-api)

clean:
	docker container rm $(shell docker container ls -a -q --filter ancestor=isabellerosa/planets-api)

stop-clean: stop clean

rmi:
	docker image rm $(shell docker image ls -q --filter reference="isabellerosa/planets-api:*")