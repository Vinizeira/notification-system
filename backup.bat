@echo off
set "BACKUP_DIR=C:\Astecob\backups"
if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"

set DATA=%date:~6,4%-%date:~3,2%-%date:~0,2%
set ARQUIVO_BACKUP=%BACKUP_DIR%\backup_astecob_%DATA%.sql

echo Gerando backup do banco de dados no container 'aviso-guias-postgres'...

docker exec -i aviso-guias-postgres pg_dump -U aviso_guias_user -d aviso_guias > "%ARQUIVO_BACKUP%"

echo.
echo Backup concluido em: %ARQUIVO_BACKUP%