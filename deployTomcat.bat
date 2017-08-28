set OLD_DIR=%cd%

rem Building the parent project
call mvn clean install -Ptomcat
echo "Built the parent project"

cd %CATALINA_HOME%\logs
del *.log
del *.txt
echo "Deleted all logs"


cd "%CATALINA_HOME%\webapps"
del rdma.war
rmdir /s /q rdma

cd %OLD_DIR%
copy "gdma2-web\target\rdma.war" "%CATALINA_HOME%\webapps"

echo "Done!!!"