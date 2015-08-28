#!/usr/bin/env bash
set -e

if [ $(docker ps -a | grep --count pgtest_data) -eq 0 ]
then
    docker run --name pgtest_data postgres:9.4 /bin/true
fi

docker run --name pgtest -d -v $(pwd)/db:/docker-entrypoint-initdb.d --volumes-from pgtest_data -p 5432:5432 postgres:9.4
sbt flywayMigrate
docker logs -f pgtest || true
docker stop pgtest || true
docker kill pgtest || true
docker rm pgtest
