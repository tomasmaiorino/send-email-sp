pipeline {



    agent any



    stages {

        stage('build-deve') {

         when { branch "deve" }

            steps {

            sh 'echo deploying test'

            sh 'mvn test'
            
            sh 'mvn integration-test -DskipItTest=false -P it -Dsendemail.service.mailgun.mailgunKey=$MAILGUN_KEY -Dsendemail.service.mailgun.mailgunDomain=$MAILGUN_DOMAIN_NAME -Dit.test.email=$IT_TEST_EMAIL'

			sh 'git push origin deve'
            }

        }

        stage('build-sand') {

         when { branch "sand" }

            steps {

            sh 'echo deploying sand'            

sh 'mvn clean install'

sh 'mvn test'

            }

        }        

    }

}