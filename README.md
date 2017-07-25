It's an application that can be used to send contact email from your website to your email account.

This application is running under Spring Boot and as it uses mailgun's engine to send the email you need to create a mailgun's account.


## Used Technologies

**1. Java version 8.**

**2. JPA:** Mapping persistent entities in domains objects.

**3. Spring Data JPA:** It's used to generate part of the code of the persistence layer.

## Additional Technologies

**Database:** The first client needs to be insert through database in order to enable the crossdomain post to the client's hosts. A script example can be found at the end of this file.

**Tests:** The tests are defined as use case of the Junit. The tests have been made available in the structure: src/test/java.

**Spring Boot:** It is important to check the application's profiles once that its use more than one. 

**Maven:** Life cycle management and project build.

## Considerations

The site http://tomasmaiorino.github.io uses for its contact functionality this send-email service. As the applications has been running using heroku, it may take a while for the first response.
 
## Usage In Local Machine

###### Pré-requisitos
```
JDK - Java version 1.8.

Any Java IDE with support Maven.

Maven for build and dependencies.


###### After download the fonts, to install the application and test it execute the maven command:
$ mvn clean install

###### To only test the application execute the maven command:
$ mvn clean test

###### To run the integrations tests execute the maven command:
$ mvn integration-test -DskipItTest=false -P it -Dsendemail.service.mailgun.mailgunKey=${MAILGUN_KEY} -Dsendemail.service.mailgun.mailgunDomain=${MAILGUN_DOMAIN} -Dit.test.email=${IT_TEST_EMAIL}

###### To run the application the maven command:
$ mvn spring-boot:run -Dspring.profiles.active=local -Dsendemail.service.mailgun.mailgunKey=${MAILGUN_KEY} -Dsendemail.service.mailgun.mailgunDomain=${MAILGUN_DOMAIN} 

###### To send a report to the admin client using curl:
$ curl -i -H "Content-Type:application/json"  -H "AT: $ADMIN_TOKEN_VALUE" -X GET http://localhost:8080/api/v1/clients/report
Sample response:
HTTP/1.1 200 

###### To create a client using curl:
$ curl -i -H "Content-Type:application/json"  -H "AT: $ADMIN_TOKEN_VALUE" -H "Accept:application/json" -X POST http://localhost:8080/api/v1/clients -d "{\"hosts\": [\"http://localhost:8080\",\"localhost:8080\"],\"token\": \"qwetyuasdtyuer4rr\",\"email\": \"user@domain.com\",\"name\": \"Jean Gray\",\"emailRecipient\": \"user@domain.com\",\"status\":\"ACTIVE\"}
Sample response:
{
    "id": 1 ,
    "hosts": ["http://localhost:8080", "localhost:8080"],
    "token": "qwetyuasdtyu",
    "email": "user@domain.com",
    "name": "Jean Gray",
    "emailRecipient": "user@domain.com",
    "status": "ACTIVE"
}

###### To send a message using curl:
$ curl -i -H "Content-Type:application/json" -H "Accept:application/json" -H "Referer: http://localhost" -X POST http://localhost:40585/api/v1/messages/qwetyuasdtyuer4rr -d "{\"message\": \"I really enjoy your site.\",\"subject\": \"Contact\",\"name\": \"Jean Gray\",\"senderEmail\": \"user@domain.com\",\"senderName\":\"Logan\"}"
Sample response:
{
    "id": 1,
    "status": "CREATED | SENT | NOT_SENT | ERROR"
    "message": "I really enjoy your site.",
    "subject": "Contact",
    "name": "Jean Gray",
    "senderEmail": "user@domain.com",
    "senderName": "Logan"
}

## Usage In Production Machine
JDK - Java version 1.8.

Maven for build and dependencies.

###### To run the application the maven command:
$ mvn spring-boot:run -Dspring.profiles.active=prod -Dsendemail.service.mailgun.mailgunKey=${MAILGUN_KEY} -Dsendemail.service.mailgun.mailgunDomain=${MAILGUN_DOMAIN}

###### First client's insert (postgre) sample:
insert into client(id, name, token, email, status, emailRecipient, created) values (1, 'Jean Gray','qwetyuasdtyu', 'user@site.com', 'ACTIVE', 'user@site.com', current_timestamp);

insert into client_hosts(id, host, client_id, created) values (1, 'http://site.com',1, current_timestamp);