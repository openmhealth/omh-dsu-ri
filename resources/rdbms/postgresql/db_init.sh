#!/bin/bash

gosu postgres pg_ctl -w start
gosu postgres psql -U postgres < /docker-entrypoint-initdb.d/oauth2-ddl.sql
gosu postgres psql -U postgres omh < /docker-entrypoint-initdb.d/oauth2-sample-data.sql
gosu postgres pg_ctl stop
