#GeoLocationApp

#Running Configuration
In order to run the project need to install the mysql server 
-map the port which is on the application.yml file. 
-changing the username and password in application.yml file according to mysql username and password.
-while running the project run the  Main file with passing runtime argument server /application.yml

#Request

use get request with {base-url}/locations?ip={ipaddress}
example localhost:8089/locations?ip=24.48.0.1
the query parameter ip is required