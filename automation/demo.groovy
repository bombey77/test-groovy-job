pipeline {
    agent any
    stages {
        stage("Start cleaning old branches") {
            steps {
                script {
                    def repository_names = ["twobit", "sweater"]
                    for (def repository : repository_names) {
                        stage("Clean workspace for ${repository}") {
                            cleanWs()
                        }
                        stage("Clone repositories and clean old branches for ${repository}") {
                            dir(repository) {
                                def git_repository = "git@github.com:bombey77/${repository}.git"
                                git credentialsId: 'mac_ssh',
                                    url: git_repository
                                println "Cloned from ${git_repository}"

                                for (def branch : sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split('\n')) {
                                    def test = sh(script: "git log -1 --since='1 month ago' -s ${branch}", returnStatus: true)
                                    println "result = ${test}"
                                    if (sh(script: "git log -1 --since='1 month ago' -s ${branch}", returnStatus: true) != 0) {
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
    }
    post {
        always {
            deleteDir()
        }
    }
}