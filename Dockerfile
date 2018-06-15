# **********************************************************************************************************************
# Copyright  Tomas Inc., 2018.
# All Rights Reserved.
#
# Usage:
#   docker build --force-rm -t <image_name> --build-arg branch_image_name=<branch_image_name> .
# Container:
# 1 - docker create -p 40585:40585 -e profile=<profile> -e port=40585 -v /c/Users/tomas.maiorino/.m2:/root/.m2 --name <container_name> <image_name>
# 2 - docker create --link postgres --link eureka -e profile=local -e port=40666 -e custom_mvn_parameters=-Dspring.datasource.url=jdbc:postgresql://postgres:5432/send_email\ -Deureka.client.serviceUrl=http://eureka:8761/eureka -e branch_name=eureka -p 40666:40566 -v /c/Users/tomas.maiorino/.m2:/root/.m2 --name send-eureka send-eureka
# **********************************************************************************************************************
FROM maven:3-jdk-8-slim
ARG branch_image_name
MAINTAINER Maiorino Tomas <tomasmaiorino@gmail.com>

#APPLICATION CONFIGURATION
ENV APPLICATION_REPO https://github.com/tomasmaiorino/send-email-sp
ENV branch_name master
ENV profile dev
ENV port 40585
ENV custom_mvn_parameters -V

# this is a non-interactive automated build - avoid some warning messages
ENV DEBIAN_FRONTEND noninteractive

RUN echo $branch_image_name

# update dpkg repositories
RUN apt-get update

# install git
RUN apt-get -y install git

# remove download archive files
RUN apt-get clean

# setup config work dir
RUN mkdir /app
WORKDIR /app

# clone repository
RUN git clone https://github.com/tomasmaiorino/send-email-sp

WORKDIR /app/send-email-sp

# checkout to the desired branch
RUN git checkout $branch_image_name

# set the start file to an executable
RUN chmod +x starting_container.sh

#EXPOSE 40585

ENTRYPOINT ["/bin/bash", "-c", "/app/send-email-sp/./starting_container.sh ${branch_name} ${profile} ${port} ${custom_mvn_parameters}"]