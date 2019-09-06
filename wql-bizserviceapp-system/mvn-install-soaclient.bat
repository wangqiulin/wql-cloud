@echo off
echo [wql cloud] install client jar
cd /D %~dp0
set MAVEN_OPTS= -Xms256M -Xmx1024M -XX:PermSize=64M -XX:MaxPermSize=128M -Dfile.encoding=UTF-8
call mvn clean assembly:assembly
cd target
call mvn install:install-file -Dfile=wql-bizserviceapp-system-0.0.1-SNAPSHOT.jar -DgroupId=com.wql.cloud -DartifactId=wql-soaclient-system -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
pause