# **********************************************************************************************************************
# Copyright  Tomas Inc., 2018.
# All Rights Reserved.
#
# Usage:
#   docker build -t <image_name> --build-arg branch_name=<branch_name> .
# **********************************************************************************************************************
FROM maven:3-jdk-8-slim
ARG branch_name
MAINTAINER Maiorino Tomas <tomasmaiorino@gmail.com>

#APPLICATION CONFIGURATION
ENV APPLICATION_REPO https://github.com/tomasmaiorino/send-email-sp

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
RUN git checkout $branch_name
RUN git pull origin $branch_name
RUN mvn clean install

EXPOSE 8080

CMD [""]
