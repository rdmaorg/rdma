set OLD_DIR=%cd%

rem Building the parent project
call mvn clean install -Pwildfly
echo "Built the parent project"


cd %OLD_DIR%
copy "gdma2-web\target\rdma.war" "%JBOSS_HOME%\standalone\deployments"

echo "Done!!!"