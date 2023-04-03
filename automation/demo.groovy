println "Hello world!!!"

def repository_names = ["twobit", "sweater"]
def src = "src"

if (!fileExists(src)) {
    mkdir src
} else {
    deleteDir()
    mkdir src
}

dir(src) {
    for (def repository : repository_names) {
        def git_repository = "git@github.com:bombey77/${repository}.git"
        sh "git clone ${git_repository}"
        println "Cloned from ${git_repository}"
        dir(repository) {
            sh "git fetch --prune"
            for (def branch : sh(script: "git branch -r | grep -vE 'master|main'", returnStdout: true).trim().split('\n')) {
                if (sh(script: "git log -1 --since='1 day ago' -s ${branch}", returnStatus: true) != 0) {
                    def remote_branch = branch.replaceAll("origin/", "")
                    println "Branch name to remove - ${remote_branch}"
                }
            }
        }
    }
}

println "Done!!!"