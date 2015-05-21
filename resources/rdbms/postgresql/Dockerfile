FROM postgres:latest
MAINTAINER Matthew Schulkind <mschulkind@gmail.com>

ADD oauth2-ddl.sql oauth2-sample-data.sql /docker-entrypoint-initdb.d/
ADD db_init.sh /docker-entrypoint-initdb.d/
