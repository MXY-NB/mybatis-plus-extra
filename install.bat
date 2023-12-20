@echo on
rem 
cd %~dp0
call mvn -e -Dmaven.test.skip=true clean install
cmd