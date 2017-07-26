pipeline {



    agent any



    stages {

        stage('build-deve') {

         when { branch "deve" }

            steps {

            sh 'echo deploying test'

            sh 'echo $IT_TEST_EMAIL'

            sh 'mvn test -Dsendemail.service.mailgun.mailgunKey=$IT_TEST_EMAIL'

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