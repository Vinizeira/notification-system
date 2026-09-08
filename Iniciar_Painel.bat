@echo off
echo =========================================
echo    Iniciando o Painel Astecob...
echo    Por favor, nao feche esta janela preta.
echo =========================================
timeout /t 3 >nul

start http://localhost:8080/dashboard
java -jar painel-astecob.jar

pause