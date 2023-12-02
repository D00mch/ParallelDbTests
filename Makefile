PG_BASE = .docker/postgres
PG_INIT = ${PG_BASE}/initdb.d

docker-prepare:
	/bin/rm -rf ${PG_BASE}
	mkdir -p ${PG_INIT}
	cp resources/migrations/init.sql ${PG_INIT}/00000000000000-init.sql

docker-up: docker-prepare
	docker-compose up

docker-down:
	docker-compose down --remove-orphans

docker-rm:
	docker-compose rm --force

docker-psql:
	psql --port 5432 --host localhost -U arep arep-dev
