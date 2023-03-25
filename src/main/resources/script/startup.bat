@echo off

set JAVA_OPTIONS=-Xms512M -Xmx512M -XX:+HeapDumpOnOutOfMemoryError
for /f "usebackq" %%i in (`dir kx-game*REL.jar /b`) do set PRJ_JAR=%%i

xjar java %JAVA_OPTIONS%  -jar  %PRJ_JAR% --spring.profiles.active=pro

pause