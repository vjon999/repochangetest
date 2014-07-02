mvn clean package
mv target/apps.war ../../../Servers/apache-tomcat-7.0.41/webapps/
sh ../../../Servers/apache-tomcat-7.0.41/bin/catalina.sh run