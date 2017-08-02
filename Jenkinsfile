pipeline {
    agent any
    stages {
        stage('build-common') {
        	steps {
            }
        }
        stage('build-deve') {
         when { branch "deve" }
            steps {
            	sh 'git fetch'
            	sh 'git checkout deve'
	            sh 'mvn clean install'
	            sh 'mvn test'
	            sh 'mvn verify -DskipItTest=false -P it -Dsendemail.service.mailgun.mailgunKey=$MAILGUN_KEY -Dsendemail.service.mailgun.mailgunDomain=$MAILGUN_DOMAIN_NAME -Dit.test.email=$IT_TEST_EMAIL'
            	sh 'git push origin deve'
            	sh 'git checkout sand'
            	sh 'git merge origin deve'
				sh 'git push origin sand'
            }
        }
        stage('build-sand') {
         when { branch "sand" }
            steps {
            	sh 'git checkout master'
            	sh 'git merge sand'
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