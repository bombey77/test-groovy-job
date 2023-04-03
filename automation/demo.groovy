pipeline {
    agent any
    stages {
        stage('Clean GIT branches') {
            steps {
                script {
                    def repository_names = ["twobit"]
                    def src = "src"

                    if (!fileExists(src)) {
                        sh "mkdir $src"
                    } else {
                        deleteDir()
                        sh "mkdir $src"
                    }

                    dir(src) {
                        for (def repository : repository_names) {
                            def git_repository = "git@github.com:bombey77/${repository}.git"
                            git credentialsId: '04fe517d-3cf7-4aa3-8b40-5aa169e7e973',
                                url: 'git@github.com:bombey77/twobit.git',
                                branch: 'main'
                            sh "git clone ${git_repository}"
                            println "Cloned from ${git_repository}"
                            dir(repository) {
                                for (def branch : sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split('\n')) {
                                    if (sh(script: "git log -1 --since='1 day ago' -s ${branch}", returnStatus: true) != 0) {
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
            println "Done!!!"
        }
    }
}