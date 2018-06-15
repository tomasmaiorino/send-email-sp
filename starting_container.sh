#!/bin/bash
echo ######################################################
echo parameters branch: $1 profile: $2 port: $3 custom: $4
echo ######################################################
echo
echo ------------------------
echo checkout branch $1
echo ------------------------
echo
git checkout $1

echo
echo ------------------------
echo pulling repository $1
echo ------------------------
echo
git pull origin $1

echo
echo ------------------------
echo cleaning and installing
echo ------------------------
echo
mvn clean install

echo
echo ------------------------
echo starting app to profile $2 port $3 custom $4
echo ------------------------
echo
mvn spring-boot:run -Dspring.profiles.active=$2 -Dserver.port=$3 $*
