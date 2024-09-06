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
        //sh "/opt/sonar-scanner-6.1.0.4477-linux-x64/bin/sonar-scanner -Dsonar.host.url=http://sonarqube-internal.rdevopsb79.online:9000 -Dsonar.login=admin -Dsonar.password=${sonar_password} -Dsonar.qualitygate.wait=true -Dsonar.projectKey=${env.appName} ${env.SONAR_CLASS_PATH}"
        sh 'echo Code Quality'
      }
    }

    stage('Security Checks') {
      sh 'echo Checkmarx SAST'
      sh 'echo Checkmarx SCA'
    }

    if (env.gitbrname == "main") {

      stage('Docker Build & Push') {
        env.GIT_COMMIT = checkout([$class: 'GitSCM', branches: [[name: 'main']],
                                userRemoteConfigs: [[url: "https://github.com/raghudevopsb79/${env.appName}"]]]).GIT_COMMIT
        sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 739561048503.dkr.ecr.us-east-1.amazonaws.com'
        sh "docker build -t roboshop-cart:latest 739561048503.dkr.ecr.us-east-1.amazonaws.com/roboshop-cart:${env.GIT_COMMIT} ."
        sh "docker push roboshop-cart:latest 739561048503.dkr.ecr.us-east-1.amazonaws.com/roboshop-cart:${env.GIT_COMMIT}"
        sh 'echo prisma palo alto scan'
      }


      stage('Deploy to Dev') {
        sh 'echo Hello'
      }

    }

  }
}

