#!/bin/bash
echo ######################################################
echo parameters branch: $1 profile: $2 port: $3 custom: $*
echo ######################################################
echo
echo ------------------------
echo checkout: git checkout $1
echo ------------------------
echo
git checkout $1

echo
echo ------------------------
echo pulling: git pull origin $1
echo ------------------------
echo
git pull origin $1

echo
echo ------------------------
echo cleaning and installing: mvn clean install
echo ------------------------
echo
mvn clean install

echo
echo ------------------------
echo mvn start: mvn spring-boot:run -Dspring.profiles.active=$2 -Dserver.port=$3 $*
echo ------------------------
echo
mvn spring-boot:run -Dspring.profiles.active=$2 -Dserver.port=$3 $*
