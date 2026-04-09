pipeline {
    agent any

    tools {
        maven "M391"
        // Добавь это (имя должно совпадать с тем, что в Global Tool Configuration)
        allure "allure2"
    }

    stages {
        stage('Env Prep') {
            steps {
                bat 'mvn -version'
            }
        }

        stage('Checkout & Build') {
            steps {
                // Если ты в режиме "Pipeline from SCM", Jenkins уже скачал код.
                // Если хочешь "чистый" билд, делай так:
                deleteDir()
                checkout scm

                // Теперь Maven точно найдет pom.xml
                bat 'mvn clean test -Dtest=CucumberTestRunner'
            }
        }
    }

    post {
        always {
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true

            // results: [[path: ...]] — путь должен вести туда, куда Maven складывает allure-json файлы
            allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
    }
}
