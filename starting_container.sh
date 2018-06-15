#!/bin/bash
echo checkout branch $1 ->
git checkout $1
echo pulling repository $1 ->
git pull origin $1
echo cleaning and installing ->
mvn clean install
echo starting app for profile $2 port $3 ->
mvn spring-boot:run -Dspring.profiles.active=$2 -Dserver.port=$3