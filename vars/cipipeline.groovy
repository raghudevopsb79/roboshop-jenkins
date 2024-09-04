def call() {

  node() {
    stage(Build) {
      sh 'ls -l'
    }
  }







//  pipeline {
//    agent any
//
//    stages {
//
//      stage('Build') {
//        steps {
//          sh 'echo Hello'
//        }
//      }
//
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