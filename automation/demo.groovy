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
                        stage("Clone repository and clean old branches for ${repository}") {
                            dir(repository) {
                                def git_repository = "git@github.com:bombey77/${repository}.git"
                                git credentialsId: 'mac_ssh',
                                    url: git_repository
                                println "Cloned from ${git_repository}"

                                def branchesStatus = sh(script: "git branch -r | grep -vE 'master|main'", returnStatus: true)
                                if (branchesStatus == 0) {
                                    def branches = sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split("\n")
                                    for (branch in branches) {
                                        def lastCommitDateStatus = sh(script: "git log -1 --since='1 month ago' -s ${branch}", returnStatus: true)
                                        if (lastCommitDateStatus == 0) {
                                            def lastCommitDate = sh(script: "git log -1 --since='1 month ago' -s ${branch}", returnStdout: true).trim()
                                            if (lastCommitDate.isEmpty()) {
                                                def remoteBranch = branch.replaceAll("origin/", "")
                                                println "Branch name to remove - ${remoteBranch}"
                                                // sh(script: "git push origin -d ${remoteBranch}")
                                            }
                                        }
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