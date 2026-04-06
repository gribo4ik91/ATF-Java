@echo off
chcp 65001

echo JAVA_HOME и PATH установлены на время сессии.

echo === Запуск Cucumber тестов ===
mvn clean test -Dtest=CucumberTestRunner

echo.
echo === Генерация Allure отчёта ===
mvn allure:report
echo === Готово! Открой отчет:
echo target\site\allure-maven-plugin\index.html

echo Открой вручную в браузере:
echo http://localhost:63342/ATF-Java/Testing-framework/target/site/allure-maven-plugin/index.html


pause
