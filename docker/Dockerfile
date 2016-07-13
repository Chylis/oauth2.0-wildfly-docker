# Base this image on jboss/wildfly 10.0.0.Final (uses the more "enterprise" OS centos7)
FROM jboss/wildfly:10.0.0.Final

MAINTAINER "Magnus Eriksson" <chylis88@gmail.com>

#################
##### README ####
#################

# This image starts a wildfly server and links with a container running a mysql db.

# 1) List containers:                   docker ps -a
# 2) Remove containers:                 docker rm oauth mysqldb
# 3) List images:                       docker images
# 4) Remove oauth image:                docker rmi <image id>
# 5) Build oauth image tag 0.1:         docker build -t="chylis88/oauth-wildfly:0.1" .
# 6) Push image to docker hub           docker push chylis88/oauth-wildfly:0.1
# 7) Update the docker-compose.yml file to use the image from step 6
# 8) Run the docker-compose: docker-compose up
# 9) Tail the oauth logs:               docker logs -f oauth
# 10) Fetch the ip                      docker-machine ip
# 11) Create a user:                    curl <ip>:8080/oauth/api/test/unauthenticated
# 12) Check the mysql user table:       docker exec -it mysqldb bash
# 13) Connect to the mysql db:          mysql -uaouth -p oauth, use oauth; select * from User;


USER root
COPY customization /opt/jboss/wildfly/customization/
RUN chmod +x /opt/jboss/wildfly/customization/setup_wildfly.sh
CMD ["/opt/jboss/wildfly/customization/setup_wildfly.sh"]