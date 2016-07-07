OAuth2.0 Authorization Code Grant + OpenId Connect  Demo.

Setup instructions:
Intellij:
- Import project from sources
- Select pom.xml
- Environment settings -> Select maven > 3.1.0
- Install IntelliJ Plugin 'Lombok'

The flow goes:
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
