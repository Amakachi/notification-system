# Notification System

### Description
This service creates a HTTP notification system. 
A server (or set of servers) will keep track of topics -> 
subscribers where a topic is a string and a subscriber is an HTTP endpoint. 
When a message is published on a topic, it is forwarded to all subscriber endpoints. The application runs on port 8000

### Technology
* Java 11
* Spring boot 
* Maven
* Junit 5
* H2 database
* Swagger

### Documentation
Swagger - http://localhost:8000/swagger-ui.html


