# **********************************************************************************************************************
# Copyright  Tomas Inc., 2018.
# All Rights Reserved.
#
# Usage:
#   docker build --force-rm -t <image_name> --build-arg branch_image_name=<branch_image_name> .
# Container:
#	docker create -p 40585:40585 -e profile=<profile> -e port=40585 -v /c/Users/tomas.maiorino/.m2:/root/.m2 --name <container_name> <image_name>
# **********************************************************************************************************************
FROM maven:3-jdk-8-slim
ARG branch_name
MAINTAINER Maiorino Tomas <tomasmaiorino@gmail.com>

#APPLICATION CONFIGURATION
ENV APPLICATION_REPO https://github.com/tomasmaiorino/send-email-sp
ENV branch_name master
ENV profile dev
ENV port 40585

# this is a non-interactive automated build - avoid some warning messages
ENV DEBIAN_FRONTEND noninteractive

# update dpkg repositories
RUN apt-get update

# install git
RUN apt-get -y install git

# remove download archive files
RUN apt-get clean

RUN mkdir /app
WORKDIR /app
RUN git clone https://github.com/tomasmaiorino/send-email-sp
WORKDIR /app/send-email-sp
#RUN git checkout $branch_image_name

#EXPOSE 40585

CMD ["/bin/bash", "/app/send-email-sp/./starting_container.sh ${branch_name} ${profile} ${port}"]
