pipeline {
    agent any

    environment {
        MAILGUN_KEY = 'true'
        MAILGUN_DOMAIN    = 'sqlite'
        IT_TEST_EMAIL = ''
    }

    stages {
        stage('build') {
            steps {
                if (env.BRANCH_NAME == "deve") {
                	sh 'echo env.BRANCH_NAME'
                } else if (env.BRANCH_NAME == "sand") {
                	sh 'echo env.BRANCH_NAME'
                } else {
                	sh 'echo env.BRANCH_NAME'
                }
            }
        }
    }
}