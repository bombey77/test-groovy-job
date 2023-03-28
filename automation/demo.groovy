println "Hello world!!!"

pipeline {
    agent any
    stages {
        stage('Clean GIT branches') {
            steps {
                sh "pwd"
                // dir("${WORKSPACE}"){
                //     sh './clean-git-branches.sh'
                // }
            }
        }
    }
}

println "Done!!!"