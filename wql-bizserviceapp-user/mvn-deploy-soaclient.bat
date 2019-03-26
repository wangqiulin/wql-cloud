@echo off
cd /D %~dp0
echo [wql cloud] deploy client jar
set MAVEN_OPTS= -Xms256M -Xmx1024M -XX:PermSize=64M -XX:MaxPermSize=128M -Dfile.encoding=UTF-8
call mvn clean assembly:assembly
cd target
call mvn deploy:deploy-file -Dfile=serviceapp-user-0.0.1-SNAPSHOT.jar -DgroupId=com.wql.cloud -DartifactId=wql-soaclient-user -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DrepositoryId=deploySnapshot -Durl=http://nexus.zanfin.com/repository/maven-snapshots/
REM call mvn deploy:deploy-file -Dfile=bizserviceapp-user-0.0.1.jar -DgroupId=com.wql.cloud -DartifactId=wql-soaclient-user -Dversion=0.0.1 -Dpackaging=jar -DrepositoryId=deployRelease -Durl=http://nexus.zanfin.com/repository/maven-releases/
pause