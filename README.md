# Dev Commands

```
Run postgres: 
docker exec --user postgres -it agentic-postgres-1 psql

Run db init script:
docker exec --user postgres agentic-postgres-1 psql -a -f /docker-entrypoint-initdb.d/init.sql

Run server:
docker compose up server
```
