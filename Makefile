.PHONY: all run clean build

all: build run
	echo 'build maven project and run locally in k8s'

build:
	mvn clean install package

run:
	docker -t aps-backend:v1-alpha build .

clean:
	mvn clean
