pipeline {
    agent any
    stages {
        stage('Clone GIT repositories and clean branches') {
            steps {
                script {
                    def repository_names = ["twobit", "sweater"]
                    for (def repository : repository_names) {
                        dir(repository) {
                            def git_repository = "git@github.com:bombey77/${repository}.git"
                            git credentialsId: 'mac_ssh',
                                url: git_repository
                            println "Cloned from ${git_repository}"

                            for (def branch : sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split('\n')) {
                                if (sh(script: "git log -1 --since='1 day ago' -s ${branch}", returnStatus: true) == 0) {
                                    def remote_branch = branch.replaceAll("origin/", "")
                                    println "Branch name to remove - ${remote_branch}"
                                }
                            }
                        }
                        stage("Clean workspace after ${repository}") {
                            cleanWs()
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            deleteDir()
        }
    }
}