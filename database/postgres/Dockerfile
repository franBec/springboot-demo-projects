FROM postgres:17-alpine
COPY init-users.sh /docker-entrypoint-initdb.d/01-init-users.sh
RUN chmod +x /docker-entrypoint-initdb.d/01-init-users.sh
