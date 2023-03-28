println "Hello world!!!"

pipeline {
    agent any
    stages {
        stage('Clean GIT branches') {
            steps {
                sh "echo pwd"
                sh "mkdir src"
                // dir("${WORKSPACE}"){
                //     sh './clean-git-branches.sh'
                // }
            }
        }
    }
}

println "Done!!!"