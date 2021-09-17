# Subscribe Service

## Description
This service starts running on port 9000 (subscriber). 
The subscriber will be getting data forwarded to it when its corresponding topic is published on port 8000, 
which it will then receive and print the data to verify everything is working at the test1 and test2 endpoints

## Technology
* Java 11
* Spring boot
* Maven

## Endpoints
* test1 - "/test1"
* test2 - "/test2"