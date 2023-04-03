pipeline {
    agent any
    stages {
        stage('Clean workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Git credentials') {
            steps {
                git credentialsId: '04fe517d-3cf7-4aa3-8b40-5aa169e7e973',
                    url: 'git@github.com:bombey77/twobit.git',
                    branch: 'main'
            }
        }

    }
    post {
        always {
            println "Done!!!"
        }
    }
}