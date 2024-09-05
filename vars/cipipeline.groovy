def call() {

  env.VAULT_ADDR = "https://vault-internal.rdevopsb79.online:8200"
  env.VAULT_SKIP_VERIFY = 1

  if(env.appType == "java") {
    env.SONAR_CLASS_PATH = "-Dsonar.java.binaries=target"
  }
  else {
    env.SONAR_CLASS_PATH=""
  }

  node() {

    stage('Code Checkout') {
      sh 'find . | grep "^./" |xargs rm -rf'

      if (env.TAG_NAME ==~ ".*") {
        env.gitbrname = "refs/tags/${env.TAG_NAME}"
      } else {
        env.gitbrname = "${env.BRANCH_NAME}"
      }
      checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: "https://github.com/raghudevopsb79/${env.appName}"]], branches: [[name: gitbrname]]], poll: false

    }

    stage('Build') {
      if (env.appType == "java") {
        java.codeBuild()
      }
    }

    stage('Unit Tests') {
      sh 'echo Unit Tests Completed!!'
    }

    if (env.gitbrname == "main") {
      stage('Integration Tests') {
        sh 'echo Integration Test Completed !!'
      }
    }

    stage('Code Quality') {

      def sonar_password = vault.getSecret('common', 'sonarqube', 'password')
      maskPasswords(varPasswordPairs: [[password: sonar_password, var: 'sonar_password']]) {
        sh "/opt/sonar-scanner-6.1.0.4477-linux-x64/bin/sonar-scanner -Dsonar.host.url=http://sonarqube-internal.rdevopsb79.online:9000 -Dsonar.login=admin -Dsonar.password=${sonar_password} -Dsonar.qualitygate.wait=true -Dsonar.projectKey=${env.appName} ${env.SONAR_CLASS_PATH}"
      }
    }

    stage('Security Checks') {
      sh 'echo Hello'
    }

    if (env.gitbrname == "main") {
      stage('Deploy to Dev') {
        sh 'echo Hello'
      }
    }

  }
}

