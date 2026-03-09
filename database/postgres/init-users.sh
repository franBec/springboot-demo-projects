#!/bin/bash
set -e

echo "Creating sakila_app user with DML-only privileges..."

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Create restricted application user
    CREATE USER sakila_app WITH PASSWORD '$SAKILA_APP_PASSWORD';
    
    -- Grant connection to database
    GRANT CONNECT ON DATABASE $POSTGRES_DB TO sakila_app;
EOSQL

echo "sakila_app user created successfully"
echo "Privileges will be granted by Flyway migration V3 after tables are created"
