# Open mHealth DSU RI

This repository contains a Java reference implementation of the Open mHealth DSU 1.1 specification. It consists of a
 resource and authorization server.              
 
### Installation

There are two ways to build and run the server. 

1. You can run a Docker container that executes a pre-built binary. 
  * This is the fastest way to get up and running and isolates the install from your system.
1. You can build all the code from source and run it on your host system.
  * This is a quick way to get up and running if your system already has MongoDB and is prepped to build Java code. 

#### Option 1. Running a pre-built binary in Docker

If you don't have Docker installed, download [Docker](https://docs.docker.com/installation/#installation/) 
 and follow the installation instructions for your platform.

Then in a terminal,

1. If you don't already have a MongoDB container, download one by running
  * `docker pull mongo:latest`
  * Note that this will download around 400 MB of Docker images.
1. If your MongoDB container isn't running, start it by running
  * `docker run --name some-mongo -d mongo:latest`
1. Download the server image by running
  * `docker pull openmhealth/dsu-ri:latest` 
1. Start the server by running
  * `docker run --link some-mongo:mongo -d -p 8083:8083 'openmhealth/dsu-ri:latest'`

#### Option 2. Building from source and running on your host system

If you prefer not to use Docker,  

1. You must have a [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html/) or higher JDK installed. 
1. A running [MongoDB](http://http://docs.mongodb.org/manual/) installation is required.
1. [Gradle](http://www.gradle.org/) is required to build the source code.  

Then,

1. Clone this Git repository.
1. Navigate to the `src/main/resources` directory and edit the `application.properties` file.
1. Check that the `spring.data.mongodb` properties point to your running MongoDB instance.
  * You might need to change the `host` property to `localhost`, for example.
1. To build and run the server, run `gradle bootRun`
1. The server should now be running on `localhost` on port 8083. You can change the port number in the `application.properties` file.
                           