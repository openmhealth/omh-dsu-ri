# Open mHealth DSU RI

This repository contains a Java reference implementation of an [Open mHealth](http://www.openmhealth.org/) DSU. This DSU supports the
1.0.M1 data point API.

> This code is in its early stages and requires further work and testing. Please do not use it in production without proper testing.

### tl;dr

* this repository contains a secure endpoint that offers an API for storing and retrieving data points
* data points conform to the Open mHealth [data point schema](http://www.openmhealth.org/developers/schemas/#data-point)
* the code consists of an [OAuth 2.0](http://oauth.net/2/) authorization server and resource server
* the authorization server manages access tokens
* the resource server implements the data point API documented [here](docs/raml/API.yml)
* the servers are written in Java using the [Spring Framework](http://projects.spring.io/spring-framework/), [Spring Security OAuth 2.0](http://projects.spring.io/spring-security-oauth/) and [Spring Boot](http://projects.spring.io/spring-boot/)
* the authorization server needs [PostgreSQL](http://www.postgresql.org/) to store client credentials and access tokens, and [MongoDB](http://www.mongodb.org/) to store user accounts
* the resource server needs PostgreSQL to read access tokens and MongoDB to store data points
* you can pull Docker containers for both servers from our [Docker Hub page](https://registry.hub.docker.com/repos/openmhealth/)
* you can use a Postman [collection](https://www.getpostman.com/collections/18e6065476d59772c748) to easily issue API requests
  
### Overview

A *data point* is a JSON document that represents a piece of data and conforms to
the [data-point](http://www.openmhealth.org/developers/schemas/#data-point) schema. The header of a data point conforms to
  the [header](http://www.openmhealth.org/developers/schemas/#header) schema, and the body can conform to any schema you like.
The header is designed to contain operational metadata, such as identifiers and provenance, whereas the body contains
the data being acquired or computed.

The *data point API* is a simple RESTful API that supports the creation, retrieval, and deletion of data points. The
API authorizes access using OAuth 2.0.

This implementation uses two components that reflect the [OAuth 2.0 specification](http://tools.ietf.org/html/rfc6749).
A *resource server* manages data point resources and implements the data point API. The resource server authorizes
requests using OAuth 2.0 access tokens. An *authorization server* manages the granting of access tokens.


### Installation

There are two ways to build and run the authorization and resource servers. 

1. You can run a Docker container for each server that executes a pre-built binary. 
  * This is the fastest way to get up and running and isolates the install from your system.
1. You can build all the code from source and run it on your host system.
  * This is a quick way to get up and running if you don't need isolation and your system already has MongoDB, PostgreSQL and is prepped to build Java code. 

#### Option 1. Running a pre-built binary in Docker

If you don't have Docker 1.2+ installed, download [Docker](https://docs.docker.com/installation/#installation/) 
 and follow the installation instructions for your platform.

The rest of the dependencies are handled via Docker. In a terminal,

1. If you don't already have a MongoDB image, download one by running
    * `docker pull mongo:latest`
    * This will download up to 400 MB of images.
1. If a MongoDB container isn't running or if you want a new container, start one by running
    * `docker run --name omh-mongo -d mongo:latest`
1. If you don't already have a PostgreSQL image, download one by running
    * `docker pull postgres:latest`
    * This will download up to 200 MB of images.
1. If a PostgreSQL container isn't running or if you want a new container, start one by running
    * `docker run --name omh-postgres -d postgres:latest`
1. To source the Spring Security OAuth schema in the PostgreSQL container
    * If you're on Docker 1.3
        1. Run `docker exec -it omh-postgres bash` to start a shell on the `omh-postgres` container
        1. Run `psql -U postgres` in the resulting shell to start `psql`
        1. Copy and paste the contents of the [setup script](resources/rdbms/postgresql/oauth2-ddl.sql) to create the schema.
        1. `\q` to exit `psql`
        1. `exit` to exit the shell
    * If you're on an older version of Docker
        1. Start `psql` in its own container using `docker run -it --link omh-postgres:postgres --rm postgres sh -c 'exec psql -h "$POSTGRES_PORT_5432_TCP_ADDR" -p "$POSTGRES_PORT_5432_TCP_PORT" -U postgres'`
        1. Copy and paste the contents of the [setup script](resources/rdbms/postgresql/oauth2-ddl.sql) to create the schema.
        1. `\q` to exit `psql`
1. Download the authorization server image by running
    * `docker pull openmhealth/omh-dsu-authorization-server:latest`
    * This will download up to 300 MB of Docker images, 25 MB of which is the application.
1. Download the resource server image by running
    * `docker pull openmhealth/omh-dsu-resource-server:latest`
    * This will download up to 300 MB of Docker images, 25 MB of which is the application.
1. Start the authorization server by running
    * `docker run --name omh-dsu-authorization-server --link omh-postgres:omh-postgres --link omh-mongo:omh-mongo -d -p 8082:8082 'openmhealth/omh-dsu-authorization-server:latest'`
1. Start the resource server by running
    * `docker run --name omh-dsu-resource-server --link omh-postgres:omh-postgres --link omh-mongo:omh-mongo -d -p 8083:8083 'openmhealth/omh-dsu-resource-server:latest'`

#### Option 2. Building from source and running on your host system

We will add documentation on running the servers on your host system shortly.

### Configuring the authorization server

The authorization and resource servers are configured in YAML using [Spring Boot conventions](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config).
You can see the authorization server's configuration [here](authorization-server/src/main/resources/application.yml) and the
resource server's configuration [here](resource-server/src/main/resources/application.yml).

If you want to override the default configuration and you're running the servers on your host system, you can either

* Add an environment variable with `-e`to the Docker `run` command corresponding to the property you want to set.
    * e.g. `docker run --name omh-dsu-authorization-server -e logging.level.org.springframework=DEBUG ...`
    * e.g. `docker run --name omh-dsu-resource-server -e spring.data.mongodb.database=bar ...`
* Create an `application.yml` file in the `/opt/omh-dsu-ri/*-server` directory with the overriding YAML properties.
    * This is particularly useful because you can change the configuration without running new containers.
    * You may need to install an editor, e.g. using `apt-get install vim`.


#### Adding clients

The authorization server manages the granting of access tokens to clients according to the OAuth 2.0 specification. Since it is
good practice to not roll your own security infrastructure, we leverage [Spring Security OAuth 2.0](http://projects.spring.io/spring-security-oauth/)
in this implementation. You can find the Spring Security OAuth 2.0 developer guide [here](http://projects.spring.io/spring-security-oauth/docs/oauth2.html).

> It is beyond the scope of this document to explain the workings of OAuth 2.0, Spring Security and Spring Security OAuth.
> The configuration information in this document is meant to help you get started, but is in no way a replacement
> for reading the documentation of the respective standards and projects.

The authorization server uses Spring Security OAuth 2.0's `JdbcClientDetailsService` to store OAuth 2.0 client credentials.
This necessitates access to a PostgreSQL database, although we intend to release a MongoDB service down the road to
make this dependency optional.

The client details in the `oauth_client_details` table controls the identity and authentication of clients, the grant
types they can use to show they have been granted authorization, and the resources they can access and actions they
can take once they have an access token. Specifically, the client details table contains

* the identity of the client (column `client_id`)
* the resource identifiers (column `resource_ids`) the client can access , `dataPoints` in our case
* the client secret, if any (column `client_secret`)
* the scope (column `scope`) to which the client is limited, in our case some comma-separated combination of
    * `read_data_points` if the client is allowed to read data points
    * `write_data_points` if the client is allowed to write data points
    * `delete_data_points` if the client is allowed to delete data points
* the authorization grant types (column `authorized_grant_types`) the client is limited to, some comma-separated combination of
    * `authorization_code`, documented in the [Authorization Code](http://tools.ietf.org/html/rfc6749#section-1.3.1) section of the OAuth 2.0 spec
    * `implicit`, documented in the [Implicit](http://tools.ietf.org/html/rfc6749#section-1.3.2) section
    * `password`, documented in the [Resource Owner Password Credentials](http://tools.ietf.org/html/rfc6749#section-1.3.3) section of the OAuth 2.0 spec
    * `refresh_token`, documented in the [Refresh Token](http://tools.ietf.org/html/rfc6749#section-1.5) section
    * N.B. the `client_credentials` grant type in the [Client Credentials](http://tools.ietf.org/html/rfc6749#section-1.3.4) section is not yet supported, but slated to be
* the Spring Security authorities (column `authorities`) the token bearer has, in our case `ROLE_CLIENT`

To create a client,

1. Connect to the `omh` PostgreSQL database using the same mechanism you used during the installation.
1. Add a row to the `oauth_client_details` table. You can find a script with a sample `INSERT` statement [here](resources/rdbms/common/oauth2-sample-data.sql).

> The remainder of this documentation is actively being worked on.

#### Adding end users

The data points accessible over the data point API belong to a user. In OAuth 2.0, this user is called the `resource owner` or `end-user`.
A client requests authorization from the authorization server to access the data points of one or more users.

The authorization server includes a simple RESTful endpoint to create users. To create a user, execute the following command

```bash
curl -H "Content-Type:application/json" --data '{"username": "testUser", "password": "testUserPassword"}' http://${DOCKER_IP}:8082/users
```

or use the Postman collection discussed below.

The user creation endpoint is primitive by design; it is only meant as a way to bootstrap a couple users
when first starting out. In general, the creation of users is typically the concern of a user management component,
not the authorization server. And it's quite common
 for integrators to already have a user management system complete with its own user account database before introducing the
 authorization server.

To integrate a user management system with the authorization server, you would

1. Disable the `org.openmhealth.dsu.controller.EndUserController`, usually by commenting out the `@Controller` annotation.
1. Provide your own implementation of either the `org.openmhealth.dsu.service.EndUserService` or the
 `org.openmhealth.dsu.repository.EndUserRepository`, populating `org.openmhealth.dsu.domain.EndUser` instances with data read from your own data stores or APIs.

### Using the resource server

The data point API is documented in a [RAML file](docs/raml/API.yml) to avoid ambiguity.

A data point looks something like this

```json
{
    "header": {
        "id": "123e4567-e89b-12d3-a456-426655440000",
        "creation_date_time": "2013-02-05T07:25:00Z",
        "schema_id": {
            "namespace": "omh",
            "name": "physical-activity",
            "version": "1.0"
        },
        "acquisition_provenance": {
            "source_name": "RunKeeper",
            "modality": "sensed"
        }
    },
    "body": {
        "activity_name": "walking",
        "distance": {
            "value": 1.5,
            "unit": "mi"
        },
        "reported_activity_intensity": "moderate",
        "effective_time_frame": {
            "time_interval": {
                "date": "2013-02-05",
                "part_of_day": "morning"
            }
        }
    }
}
```

> The remainder of this documentation is actively being worked on.

