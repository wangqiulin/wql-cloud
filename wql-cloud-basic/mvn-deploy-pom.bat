@echo off
echo [intpay cloud] update
cd /D %~dp0
TortoiseProc.exe /command:update /path:"./" /closeonend:2
call mvn deploy:deploy-file -Dfile=pom.xml -DgroupId=com.wql.cloud -DartifactId=wql-cloud-basic -Dversion=0.0.1-SNAPSHOT -Dpackaging=pom -DrepositoryId=xxx -Durl=http://xxx:xxx/repository/xxx/
pause