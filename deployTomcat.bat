set OLD_DIR=%cd%

rem Building the parent project
call mvn clean install -Ptomcat
echo "Built the parent project"


cd %OLD_DIR%
copy "gdma2-web\target\gdma2.war" "%CATALINA_HOME%\webapps"

echo "Done!!!"