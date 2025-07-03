docker run --name my-postgres \
  -e POSTGRES_PASSWORD=secret \
  -v $(pwd)/data/pg:/var/lib/postgresql/data \
  -p 5432:5432 \
  -d postgres:15

