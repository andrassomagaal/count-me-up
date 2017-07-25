# count-me-up

I used Java 8 with Spring framework. The vote for candidates and the Count Me Up result can be called via REST interface at /vote/{candatate} and /CountMeUp. Regarding to identify users there is an authentication process. The users and authorities are stored in a database, currently in an embedded H2 for testing and can be easily changed to a real one by defining a data source for it and uncomment the @Profile(“dev”) annotations to load the embedded for tests. There is a login page to authenticate before accessing the endpoints. The vote endpoint can be requested by any authenticated user while the CountMeUp endpoint available for admins only. There is a default admin – admin user added to the embedded DB. The passwords stored encoded.
How to run:
-Build with Maven (tested with 3.3.9) 
- java -jar {build target location}/count-me-up-1.0.0.jar

There is a test case implementing the mentioned scenario, so a build with tests could take a while due to the 10 million request simulation.

To do improvements:
•	The endpoint should be accessed via HTTPS to ensure an encrypted communication.
