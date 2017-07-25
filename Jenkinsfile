pipeline {

    agent any

    stages {
        stage('build-deve') {
         	when { branch "deve" }
            steps {
            	sh 'echo deploying test'
				sh 'mvn clean install'
				sh 'mvn test'
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