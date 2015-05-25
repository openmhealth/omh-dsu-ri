#!/bin/bash

gosu postgres pg_ctl -w start
gosu postgres psql -U postgres < /tmp/oauth2-ddl.sql
gosu postgres psql -U postgres omh < /tmp/oauth2-sample-data.sql
gosu postgres pg_ctl stop
