#!/bin/bash
echo ------------------------------------------------
echo paramters branch: $1 profile: $2 port: $3
echo ------------------------------------------------
echo ------------------------
echo checkout branch $1
echo ------------------------
git checkout $1
echo ------------------------
echo pulling repository $1
echo ------------------------
git pull origin $1
echo ------------------------
echo cleaning and installing
echo ------------------------
mvn clean install
echo ------------------------
echo starting app to profile $2 port $3
echo ------------------------
mvn spring-boot:run -Dspring.profiles.active=$2 -Dserver.port=$3
