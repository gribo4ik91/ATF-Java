@echo off
set "JAVA_HOME=C:\Program Files\RedHat\java-17-openjdk-17.0.11.0.9-1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
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
