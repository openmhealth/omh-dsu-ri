# This image adds the authorization schema and sample OAuth 2.0 credentials to a PostgreSQL database.
# Contributed by Matthew Schulkind <mschulkind@gmail.com>.

FROM postgres:latest
MAINTAINER Emerson Farrugia <emerson@openmhealth.org>

COPY oauth2-ddl.sql oauth2-sample-data.sql /tmp/
COPY initialize-database.sh /docker-entrypoint-initdb.d/
