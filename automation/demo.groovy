pipeline {
    agent any
    stages {
        stage('Clean workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Clean GIT branches') {
            steps {
                script {
                    def repository_names = ["twobit"]
                    def src = "src"
                    sh "mkdir $src"
                    dir(src) {
                        for (def repository : repository_names) {
                            def git_repository = "git@github.com:bombey77/${repository}.git"
                            git credentialsId: '04fe517d-3cf7-4aa3-8b40-5aa169e7e973',
                                url: 'git@github.com:bombey77/twobit.git'
                            println "Cloned from ${git_repository}"
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            println "Done!!!"
        }
    }
}