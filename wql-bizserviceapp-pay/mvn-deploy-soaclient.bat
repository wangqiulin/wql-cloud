@echo off
cd /D %~dp0
echo [wql cloud] deploy client jar
set MAVEN_OPTS= -Xms256M -Xmx1024M -XX:PermSize=64M -XX:MaxPermSize=128M -Dfile.encoding=UTF-8
call mvn clean assembly:assembly
cd target
call mvn deploy:deploy-file -Dfile=wql-bizserviceapp-pay-0.0.1.jar -DgroupId=com.wql.cloud -DartifactId=wql-soaclient-pay -Dversion=0.0.1 -Dpackaging=jar -DrepositoryId=deployRelease -Durl=http://xxx/repository/maven-releases/
pause