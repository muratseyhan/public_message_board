# Public Message Board API

A proof-of-concept RESTful JSON API for a public message board.

This toy project is built using the following key tools. 
- Spring Boot
- Spring Security, to implement JWT-based authentication/authorization
- Spring HATEOAS, used to address the HATEOAS requirement of REST.
- [HAL (Hypertext Application Language)](https://en.wikipedia.org/wiki/Hypertext_Application_Language) and [Vnd Error](https://github.com/blongden/vnd.error), used as an attempt to satisfy HATEOAS.
They were mainly picked due to the convenience of Spring HATEOAS support.
- An in-memory [H2](https://en.wikipedia.org/wiki/H2_(DBMS)) database

## Setup

This section discusses how application can be built and run, either with the provided Gradle wrapper or docker.
Use the Gradle wrapper if you would like to avoid installing docker.
Use docker if you would like to avoid installing JDK 13.

## Build and run with Gradle wrapper

You can build and run the application using the provided gradle wrapper as follows.

### Requirements

[OpenJDK 13](https://jdk.java.net/13/) needs to be installed.
If you do not have a unix shell available, the following commands would be different.

### Build with Gradle wrapper

In order to build this project, open a terminal, navigate into the directory where you have cloned this repository,
and then run the following command.

```shell script
./gradlew clean build
```

This should build the application and run all the tests.

The unit and integration tests can be run using the commands below.
Each command should print some information regarding the tests being run. 

Run both unit and integration tests with the following command.
```shell script
./gradlew clean test
```

Run only the unit tests with the following command.
```shell script
./gradlew clean test -Punit
```

Run only the integration tests with the following command.
```shell script
./gradlew clean test -Pintegration
```

Some styled html report should be available at "build/reports/tests/test/index.html" after the tests are run.

### Run the artifact

After the application is build, run the created JAR with the following command.

```shell script
java -jar build/libs/message_board-0.0.1-SNAPSHOT.jar --debug
```

## Build and run with Docker
    
You can build and run the application through docker as follows.

### Requirements
To be able to build and run the project using the instructions below,
- you need to have [docker](https://docs.docker.com/) installed (tested only with docker 19.03.2)
- [your user needs to be in the "docker" group](https://docs.docker.com/install/linux/linux-postinstall/)
- you will also need a unix shell where docker is available

### Build with Docker

In order to build this project with docker, open a terminal, navigate into the directory where you have cloned this repository,
and then run the following command.

```shell script
docker build -t muratseyhan/message_board .
```

### Run with Docker

Use the following to start the application on port 8080 and attach the terminal to the log stream.
```shell script
docker run  -p 8080:8080 --name muratseyhan_message_board muratseyhan/message_board
```

Use the following instead, if you would like to run the application detached.
```shell script
docker run  -p 8080:8080 -d --name muratseyhan_message_board muratseyhan/message_board
```

In order to stop the application, use the docker stop or kill commands as follows.
```shell script
docker kill muratseyhan_message_board
```

Remember to remove the container, if you would like to build it with the same name again.
```shell script
docker rm muratseyhan_message_board
```

## Usage

After running the application either using the gradle wrapper or docker, you should be ready to use the application.
You can use the application with curl or another tool of your liking as below.

For the sake of brevity, only the sunshine scenario is depicted below.
In general, a 400 response should be returned for invalid requests and a 401 response for unauthorized actions.
Please refer to the integration tests for a detailed description of the error scenarios.

Create a user with issuing a POST request against /users as follows.

```shell script
$ curl -i localhost:8080/users -d '{"username": "murat", "password": "123"}' -H "Content-type: application/json" -X POST

HTTP/1.1 201 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Mon, 13 Jan 2020 18:08:37 GMT
```

If the input is valid and the username is not taken, a 201 response should be received.
A Location header is not provided, as the API does not expose representations of users.

**Note: Depending on your system and if you use docker, you might need to use an IP address instead of localhost on your setup.**

After the user is successfully created, authenticate with a POST request against /authentication as follows.

```shell script
$ curl -i localhost:8080/authentication -d '{"username": "murat", "password": "123"}' -H "Content-type: application/json" -X POST

HTTP/1.1 201 
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 13 Jan 2020 18:14:53 GMT

{"token":"JWT_TOKEN"}
```

A 201 response should be received if the username and the password are correct.
The response body should provide a JWT token  with the `token` field.
The value of the `token` field in the response will need to be provided in the Authorization header in the subsequent requests.
The token value is represented as `JWT_TOKEN`, as the actual values are rather long.

Create a message with a POST request against /messages as follows.

```shell script
$ curl -i localhost:8080/messages -X POST -d '{"title": "A very good day", "body": "It has been a wonderful day."}' -H "Content-Type: application/json" -H "Authorization: Bearer JWT_TOKEN"

HTTP/1.1 201 
Location: http://localhost:8080/messages/1
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Mon, 13 Jan 2020 18:23:28 GMT
```

The Location response header should provide a URI for the created message.

Update (replace) the created message with a PUT request against the URI provided as follows.

```shell script
$ curl -i localhost:8080/messages/1 -X PUT -d '{"title": "Not a good day", "body": "It has been a terrible day."}' -H "Content-Type: application/json" -H "Authorization: Bearer JWT_TOKEN"

HTTP/1.1 204 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Date: Mon, 13 Jan 2020 18:33:54 GMT
```

A 204 No Content response should be received if the input is valid and the message is updated.

Get a representation of the message with a GET request against the message URI as follows.

```shell script
$ curl -i localhost:8080/messages/1 -H "Authorization: Bearer JWT_TOKEN"

HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Mon, 13 Jan 2020 18:41:25 GMT

{"id":1,"authorUsername":"murat","title":"Not a good day","body":"It has been a terrible day.","_links":{"self":{"href":"http://localhost:8080/messages/1"},"messages":{"href":"http://localhost:8080/messages"}}
```

The content type "application/hal+json" indicates that the response is formatted in [HAL (Hypertext Application Language)](https://en.wikipedia.org/wiki/Hypertext_Application_Language).
The response should depict the updated content of the message.
It should also have two links, i.e. `self` identifying a URI for the message, and `messages` pointing to a URI for a collection of all the messages.

In order to get all the messages, issue a GET request to the `messages` URI as follows.

```shell script
$ curl -i localhost:8080/messages -H "Authorization: Bearer JWT_TOKEN"

HTTP/1.1 200 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Mon, 13 Jan 2020 18:49:57 GMT

{"_embedded":{"messageModelList":[{"id":1,"authorUsername":"murat","title":"Not a good day","body":"It has been a terrible day.","_links":{"self":{"href":"http://localhost:8080/messages/1"},"messages":{"href":"http://localhost:8080/messages"}}}]}}
```

A HAL response with a container of messages should be received.

Remove the created message with a DELETE request as follows.

```shell script
$ curl -i localhost:8080/messages/1 -X DELETE -H "Authorization: Bearer JWT_TOKEN"

HTTP/1.1 204 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Date: Mon, 13 Jan 2020 18:55:30 GMT
```

If the message is deleted successfully, a 204 No Content response should be received with no response body.

If you try to dereference the deleted message, you should receive a 404 [Vnd Error](https://github.com/blongden/vnd.error) as follows.

```shell script
$ curl -i localhost:8080/messages/1 -H "Authorization: Bearer JWT_TOKEN"

HTTP/1.1 404 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/vnd.error+json
Transfer-Encoding: chunked
Date: Mon, 13 Jan 2020 18:57:25 GMT

{"logref":"Not Found","message":"Could not find a message with id 1","links":[]}
```

## Known issues

Below are some known issues within the project. Described issues are omitted due to convenience and time constraints.

- Some unit and integration tests are written, but test coverage is currently poor.

- The secret key, used for token signing, is stored directly in the codebase, which is a bad security practice.

- The input constraints are very loose.

- The whole application context is loaded before each integration test, which causes a significant overhead.
This is done so as a quick implementation to rebuild the database before each integration test.

- Lack of proper logging, persisted logs, monitoring, HTTP cache headers.

- Lack of separate, interdependent docker images for building, testing and running the application.

- In a production system, message deletion would better alter a value in the data store indicating that the message is no longer active,
rather than actually removing the resource from the data store.
Deleting the actual resource should also be fine from a REST standpoint, and less of an issue in a POC with no data persistence.