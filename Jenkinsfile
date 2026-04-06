pipeline {
    agent any

    tools {
        maven "M391"
    }

    stages {
        stage('Env Prep') {
            steps {
                bat 'mvn -version'
            }
        }

        stage('Checkout & Build') {
            steps {
                deleteDir()

                // Явно указываем ветку 'main' (или 'master', если в GitHub она так называется)
//                git branch: 'main', url: 'https://github.com/gribo4ik91/ATF-Java.git'

                bat 'mvn clean test -Dtest=CucumberTestRunner'
            }

            post {
                always {
                    // allowEmptyResults: true предотвратит падение билда, если XML файлы не найдены
                    junit testResults: "**/target/surefire-reports/*.xml", allowEmptyResults: true
                    // Эта команда появится только после установки Allure плагина
                    allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                }
            }
        }
    }
}