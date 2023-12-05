
## Start DB

Make sure you Docker is running and execute this command to start and initiate the Database:

```bash
make docker-up
```

## Run tests

```bash
 ./gradlew :test 
```

Both tests use different copies of the DB if you have at least 4 CPU.

## Articles

[Как распараллелить тесты с базой данных](https://habr.com/ru/users/arturdumchev/publications/articles/)
