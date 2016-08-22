#!groovy

def checkoutCode() {
    stage 'checkout'
     checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/codesqueak/Z80Processor.git']]]
}

def build() {
    stage 'build'
    sh './gradlew clean build -x test'
}

def test() {
    stage 'test'
    sh './gradlew test'
}


stage 'execute Z80 build'

node {
  checkoutCode()
  build()
  test()
}
