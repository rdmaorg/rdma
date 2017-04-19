set OLD_DIR=%cd%

rem Building the parent project
call mvn clean install -Ptomcat
echo "Built the parent project"

cd %CATALINA_HOME%\logs
del *.log
del *.txt
echo "Deleted all logs"


cd "%CATALINA_HOME%\webapps"
del gdma2.war
rmdir /s /q gdma2

cd %OLD_DIR%
copy "gdma2-web\target\gdma2.war" "%CATALINA_HOME%\webapps"

echo "Done!!!"