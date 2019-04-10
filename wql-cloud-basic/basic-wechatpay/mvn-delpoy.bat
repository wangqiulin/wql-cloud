@echo off
echo [intpay cloud] svn update
cd /D %~dp0
TortoiseProc.exe /command:update /path:"./" /closeonend:2
echo [intpay cloud] deploy jar
set MAVEN_OPTS= -Xms256M -Xmx1024M -XX:PermSize=64M -XX:MaxPermSize=128M -Dfile.encoding=UTF-8
call mvn clean deploy
pause