@echo off
title API de Cursos - Spring Boot
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo =======================================================
echo   Iniciando API de Cursos (Spring Boot + PostgreSQL)
echo   Acesse no navegador: http://localhost:8080/cursos
echo =======================================================
echo.

call .\mvnw.cmd spring-boot:run
pause
