println "Hello world!!!"

pipeline {
    agent any
    stages {
        stage('Clean GIT branches') {
            steps {
                sh './clean-git-branches.sh'
            }
        }
    }
}

println "Done!!!"