pipeline {
    agent any
    stages {
        stage("Clean workspace") {
            steps {
                cleanWs()
            }
        }
        stage("Clone GIT repositories and clean branches") {
            steps {
                script {
                    sshagent(["mac_ssh"]) {
                        def repository_names = ["twobit", "sweater"]
                        for (def repository : repository_names) {
                            def git_repository = "git@github.com:bombey77/${repository}.git"
                            sh "git clone ${git_repository}"
                            println "Cloned from ${git_repository}"
                            dir(repository) {
                                for (def branch : sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split('\n')) {
                                    if (sh(script: "git log -1 --since='1 day ago' -s ${branch}", returnStatus: true) == 0) {
                                        def remote_branch = branch.replaceAll("origin/", "")
                                        println "Branch name to remove - ${remote_branch}"
                                    }
                                }
                            }
                            sh "ls -la"
                            stage("Clean workspace after ${repository}") {
                                cleanWs()
                            }
                            sh "ls -la"
                        }
                    }
                }
//                script {
//                    def repository_names = ["twobit", "sweater"]
//                    for (def repository : repository_names) {
//                        dir(repository) {
//                            def git_repository = "git@github.com:bombey77/${repository}.git"
//                            git credentialsId: 'mac_ssh',
//                                url: git_repository
//                            println "Cloned from ${git_repository}"
//
//                            for (def branch : sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split('\n')) {
//                                if (sh(script: "git log -1 --since='1 day ago' -s ${branch}", returnStatus: true) == 0) {
//                                    def remote_branch = branch.replaceAll("origin/", "")
//                                    println "Branch name to remove - ${remote_branch}"
//                                }
//                            }
//                        }
//                        stage("Clean workspace after ${repository}") {
//                            cleanWs()
//                        }
//                    }
//                }
            }
        }
    }
//    post {
//        always {
//            deleteDir()
//        }
//    }
}