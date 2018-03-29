It's an application that can be used to send contact email from your website to your email account.

This application is running under Spring Boot and as it uses mailgun's engine to send the email you need to create a mailgun's account. This application required a mailgun account and the value of these two attributes:
MAILGUN_KEY and MAILGUN_DOMAIN.

## Used Technologies

**1. Java version 8.**

**2. POSTGRES **

**3. Spring boot 1.5.4 **

**4. Maven **  Life cycle management and project build.

**5. Docker (Optional) ** Used container manager to create an application image and the containers.

## Considerations


**Tests:** The tests are defined as use case of the Junit. The tests have been made available in the structure: src/test/java.

**Integration Tests:** The integration tests are defined as use case of the Junit. The tests have been made available in the structure: src/it/java.

## Usage In Local Machine

###### Pré-requisitos

JDK - Java version 1.8.

Docker latest version. (For docker installation).

Maven for build and dependecies. (For not docker installation).

### Using Docker

1 - To install postgres container.  
```$
docker pull postgres
```  
2 - To create container.  
```$
docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=initdb -d postgres
```  
3 - To access the postgres container.
```$
docker exec -it postgres /bin/sh
```  
4 - To postgres and to create the database send_email.  
```$
psql -U postgres
```  
```$
create database send_email;
```  
5 - To insert client configuration.
```$
insert into client(id, name, token, email, status, emailRecipient, created) values (1, <client_name>, <client_token>, <client_email>, <client_status>, <client_email_to_receive_messages>, current_timestamp);
```  
```$
insert into client_hosts(id, host, client_id, created) values (<client_host_table_id>, <client_host_page, <client_id>, current_timestamp);
```  
6 - To quit postgres.
```$
\q
```  
5 - To exit from container.  
```$
quit
```  
6 - To create application image. (This steps excute the mvn clean install automatically)  
```$
docker build -t send_email --build-arg branch_name=master .
```  
7 - To create application container and start it.  
```$
docker run --rm -it --link postgres -p 8080:8080 --name send_email send_email mvn spring-boot:run -Drun.arguments="--spring.profiles.active=container"
```  

###### To run the integrations tests through docker run this command:
```$
docker run --rm -it --name eng_it eng mvn verify -P it -Dsendemail.service.mailgun.mailgunKey=${MAILGUN_KEY} -Dsendemail.service.mailgun.mailgunDomain=${MAILGUN_DOMAIN} -Dit.test.email=${IT_TEST_EMAIL}
```

## Considerations

The site http://tomasmaiorino.github.io uses for its contact functionality this send-email service. As the applications has been running using heroku, it may take a while for the first response.


### Not using Docker

Maven for build and dependecies.


###### After download the fonts from [link github](https://github.com/tomasmaiorino/send_email), to install the application and test it execute the maven command:
```$
mvn clean instal
```

###### To only test the application execute the maven command:
```$
mvn clean test
```  

###### To run the integrations tests execute the maven command:
```$
mvn verify -DskipItTest=false -P it -Dsendemail.service.mailgun.mailgunKey=${MAILGUN_KEY} -Dsendemail.service.mailgun.mailgunDomain=${MAILGUN_DOMAIN} -Dit.test.email=${IT_TEST_EMAIL}
```

###### To run the application the maven command:
```$
mvn spring-boot:run -Dspring.profiles.active=local -Dsendemail.service.mailgun.mailgunKey=${MAILGUN_KEY} -Dsendemail.service.mailgun.mailgunDomain=${MAILGUN_DOMAIN}
```

### Service's call examples:

#### To send a report to the admin client using curl.
```$
curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/api/v1/clients/report
```

#### To create a client using curl
```$
curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/clients -d "{\"hosts\": [\"http://localhost:8080\",\"localhost:8080\"],\"token\": \"qwetyuasdtyuer4rr\",\"email\": \"user@domain.com\",\"name\": \"Jean Gray\",\"emailRecipient\": \"user@domain.com\",\"status\":\"ACTIVE\"}
```

#### To send a message using curl.
```$
curl -i -H "Content-Type:application/json" -H "Accept:application/json" -H "Referer: http://localhost" -X POST http://localhost:40585/api/v1/messages/qwetyuasdtyuer4rr -d "{\"message\": \"I really enjoy your site.\",\"subject\": \"Contact\",\"name\": \"Jean Gray\",\"senderEmail\": \"user@domain.com\",\"senderName\":\"Logan\"}
```  

#### To recover the token.
```$
curl -i -H "Content-Type:application/json" -H "Accept:application/json" -H  -X POST http://localhost:8080/api/v1/users/auth -d "{\"email\": \"<email>\",\"password\": \"<password>\"}"
```

#### To run the application the maven command.
```$
mvn spring-boot:run -Dspring.profiles.active=prod -Dsendemail.service.mailgun.mailgunKey=${MAILGUN_KEY} -Dsendemail.service.mailgun.mailgunDomain=${MAILGUN_DOMAIN}
```
