pipeline {
    agent any
    options {
        skipDefaultCheckout()
    }
    stages {
        stage('Clean workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Clone GIT repositories and clean branches') {
            steps {
                script {
                    def repository_names = ["twobit", "sweater"] 
                    for (def repository : repository_names) {
                        dir(repository) {
                            git credentialsId: '04fe517d-3cf7-4aa3-8b40-5aa169e7e973',
                                url: "git@github.com:bombey77/${repository}.git",
                                branch: "main",
                                changelog: false,
                                poll: false,
                                skipTag: true
                            sh "git checkout -B main origin/main"
                            for (def branch : sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split('\n')) {
                                if (sh(script: "git log -1 --since='1 day ago' -s ${branch}", returnStatus: true) == 0) {
                                    def remote_branch = branch.replaceAll("origin/", "")
                                    println "Branch name to remove - ${remote_branch}"
                                }
                            }
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