OAuth2.0 Authorization Code Grant + OpenId Connect  Demo.

#################
#### IntelliJ ###
#################

Setup instructions:
- Import project from sources
- Select pom.xml
- Environment settings -> Select maven > 3.1.0
- Install IntelliJ Plugin 'Lombok'




#################
## OAuth+OpenId #
#################

The OAuth/OpenId flow goes:
0) Register you app id, redirectURL and receive credentials (client id and client secret) at google

1) The client calls the '/oauth/api/google/login' endpoint exposed by the back end, which performs the authorization
request towards the service provider

2) The client is redirected to the consent screen

3) The client is at the consent screen and accepts/denies authorization

4) The client is redirected to the redirectURL

5) The server inspects the redirectURL request parameters (contains an authorization code if the user accepted) and
redirects the client accordingly (to success/failed page)

6) *if native mobile client* The client web view intercepts the URL to check if it is the success or failed URL and dismisses the web view

7) *if successful* The server exchanges the authorization code for an access token and id_token (OpenId Connect)




#################
### Auth flow ###
#################

The request authentication flow goes:

1) A sessionId is passed client <--> server through cookies
2) REST endpoints which require authentication are annotated with the custom @Authenticated annotation
3.1) The SessionAuthenticator interceptor is bound to the @Authenticated annotation through InterceptorBinding
3.2) The SessionAuthenticator intercepts every requests and checks if the session is authed or not




#################
##### Docker ####
#################

Docker files are located in the docker folder.

There are two images:
 - mysqldb
 - oauth

docker-compose does the following:
    - creates a named volume, named 'oauth-mysql-data'
    - starts the mysqldb image, which mounts the oauth-mysql-data volume as /var/lib/mysql inside the container (where MySQL by default will write its data files)
    - starts the oauth Wildfly server image

1) List containers:                   docker ps -a
2) Remove containers:                 docker rm oauth mysqldb
3) List images:                       docker images
4) Remove oauth image:                docker rmi <image id>
5) Build oauth image tag 0.1:         docker build -t="chylis88/oauth-wildfly:0.1" .
6) Push image to docker hub           docker push chylis88/oauth-wildfly:0.1
7) Update the docker-compose.yml file to use the image from step 6
8) Run the docker-compose: docker-compose up
9) Tail the oauth logs:               docker logs -f oauth
10) Fetch the ip                      docker-machine ip
11) Create a user:                    curl <ip>:8080/oauth/api/test/unauthenticated
12) Check the mysql user table:       docker exec -it mysqldb bash
13) Connect to the mysql db:          mysql -uaouth -p oauth, use oauth; select * from User;
14) List volumes:                     docker volume ls
15) Inspect volume:                   docker volume inspect docker_oauth-mysql-data