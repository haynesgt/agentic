POSTGRES_PASSWORD=tandrapheada

docker run --rm --name my-postgres \
  -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
  -v $(pwd)/data/pg:/var/lib/postgresql/data \
  -p 35432:5432 \
  -d postgres:15

