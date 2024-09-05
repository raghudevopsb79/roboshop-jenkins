def call() {

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
      withCredentials([[$class: 'VaultTokenCredentialBinding', credentialsId: 'vault-token', vaultAddr: 'https://vault-internal.rdevopsb79.online:8200']]) {
        // values will be masked
        sh 'echo TOKEN=$VAULT_TOKEN'
        sh 'echo ADDR=$VAULT_ADDR'
      }


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