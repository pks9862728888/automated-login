# automated-login
Demo project to show how you can intercept all requests from a central service and make sure no request fails due to expired login token.

# About
This project illustrates how you can refresh your session token on the fly when your session token expires.

There is a class called `RequestInterceptorService` which intercepts all the logged in requests going out of the application.

If status code of any request is 403 Forbidden, then it attempts to re-login and retry.

# Architecture
To simulate client server model, I have simulated server in server package.

Currently, I have set the login token to be refreshed every 10 seconds in class `LoginService`

So as an user when you will call the rest endpoints from client controller `DemoController` then in terminal you can see that when the session cookie expires, then RequestInterceptorService can detect that session expired, so that it can login and make the same request again.

# Instructions to run the application
Run the main class: `AutomatedLoginApplication`

In `script.sh` you will find the curl commands which you can copy and execute in command prompt to see that no request fail due to expired login.
