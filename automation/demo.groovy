println "Hello world!!!"

pipeline {
    agent any
    stages {
        stage('Clean GIT branches') {
            steps {
                sh "${WORKSPACE}/automation/clean-git-branches.sh"
            }
        }
    }
}

println "Done!!!"