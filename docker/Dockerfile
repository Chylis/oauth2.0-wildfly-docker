# 1 Base this image on jboss/wildfly
FROM jboss/wildfly

# 2 Use our version of the standalone.xml
#COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Copy mysql driver to com/mysql/main

# 3 Add our application.properties that contains system properties
#COPY application.properties /opt/jboss/wildfly/standalone/configuration

# 4 Add our war in the container's jboss's deployments directory for it to be deployed
#ADD 5050lotto-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

# 5 Create a new image with: docker build --tag=wildfly-app .
# 6 Run the container with: docker run -it -p 8080:8080 wildfly-app. Application will be deployed on the container boot.
# 7 Use curl to see the app working:  curl http://localhost:8080/5050lotto

RUN apt-get update && apt-get install -y mysql mysql-server

#Substitute the below environment variables when you run/start the docker container
ENV username usernamePlaceholder 
ENV password passwordPlaceholder
ENV database db2

ADD dbsetup.sh /
RUN chmod 644 /dbsetup.sh
RUN "/usr/bin/sh dbsetup.sh"

EXPOSE 3306
CMD ["/usr/bin/mysqld_safe"]
