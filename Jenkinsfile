pipeline {
    agent any
    stages {
        stage('build-common') {
            sh 'echo deploying test'
            sh 'mvn test'
            sh 'mvn verify -DskipItTest=false -P it -Dsendemail.service.mailgun.mailgunKey=$MAILGUN_KEY -Dsendemail.service.mailgun.mailgunDomain=$MAILGUN_DOMAIN_NAME -Dit.test.email=$IT_TEST_EMAIL'
        }
        stage('build-deve') {
         when { branch "deve" }
            steps {
			sh 'git push origin deve'
            }
        }
        stage('build-sand') {
         when { branch "sand" }
            steps {
            	sh 'git push origin sand'
            }
        }
        stage('build-master') {
         when { branch "master" }
            steps {
            	sh 'echo deploying master'
            	sh 'git push origin master'
            }
        }
    }
}