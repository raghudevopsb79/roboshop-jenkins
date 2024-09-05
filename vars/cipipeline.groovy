def call() {

  env.VAULT_ADDR="https://vault-internal.rdevopsb79.online:8200"
  env.VAULT_SKIP_VERIFY=1

  node() {

    stage('Code Checkout') {
      sh 'find . | grep "^./" |xargs rm -rf'

      if(env.TAG_NAME ==~ ".*") {
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

    if(env.gitbrname == "main") {
      stage('Integration Tests') {
        sh 'echo Integration Test Completed !!'
      }
    }

    stage('Code Quality') {

      def sonar_password = vault.getSecret('common', 'sonarqube', 'password')
      print(sonar_password)

        //sh "/opt/sonar-scanner-6.1.0.4477-linux-x64/bin/sonar-scanner -Dsonar.url=http://sonarqube-internal.rdevopsb79.online:9000 -Dsonar.login=admin -Dsoanr.password={{secrets.sonarqube_password}} -Dsonar.qualitygate.wait=true -Dsonar.projectKey=${env.appName}"
      }
  }





//      stage('Unit Tests') {
//        steps {
//          sh 'echo Hello'
//        }
//      }
//
//      stage('Integration Tests') {
//        steps {
//          sh 'echo Hello'
//        }
//      }
//
//      stage('Code Quality') {
//        steps {
//          sh 'echo Hello'
//        }
//      }
//
//      stage('Security Checks') {
//        steps {
//          sh 'echo Hello'
//        }
//      }
//      stage('Deploy to Dev') {
//        steps {
//          sh 'echo Hello'
//        }
//      }
//
//
//    }
//
//  }
//
}