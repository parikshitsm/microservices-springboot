# microservices-springboot-v1
This project is used to create Microservices(java) using SpringBoot. Below are some of the highlights of this project. 

  - Follows Event Driven Architecture 
  - Project structure classified as per domain model
  - 2 seperate projects depicting 2 microservices. 
  - Communication between microservices happens via two ways : 
      - Synchronous
      - Asynchronous (using AMQP/RabbitMQ)
  - In-memory H2 database used for quick development
  - Mockito framework used for JUnit testing.
  - Lombok jar dependency used to avoid traditional java boiler-plate code. 
  - Maven used for building and packaging.
  
Note : This is the first version (v1) of the sample application. More advanced tools usage will be coming up in next version. Stay tuned!

How to build
-------------------------------
Pre-requisites
--------------------------------
Make sure your development environment is setup as expected. Below are list of softwares needed for successful run of this application.
  - IDE (Eclipse, STS, any other of your choice)
  - Maven
  - Postman / curl for REST APIs testing
  - Web browser (preferrably latest to run the frontend UI)
  
-----------------------------------
Steps to build
----------------------------------
  - Clone the repository and save it to your local drive
  - Run the following command from your respective project folder. 
      
                mvn clean install
  
  - You can also build the project from your IDE.
 -------------------------------------------------------
 Steps to Deploy
 ------------------------------------------------------
  - Once the application is successfully compiled and created respective jar, right click on your springboot application file or right  
 click on the project and select "Run as -> Spring Boot App". 
  - This will deploy the application onto an in-built web server (Tomcat) in case of STS. Or you can deploy the jar seperately onto 
    external web server.
  
 -----------------------------------------------------------------
 Steps to Launch the application
 -----------------------------------------------------------------
  (Launch below mentioned links in any web browser of your choice)
   - microservice-1 url              : http://localhost:8080/index.html   (tomcat port is 8080 here)
   - H2 Database console url         : http://localhost:8080/h2-console   (H2 db port is 8080 here)
   - RabbitMQ management web url     : http://localhost:15672/mgmt        (if RabbitMQ is installed locally)
   
   - Launch microservice-2 on a different server port.
   - H2 Database console url (ms-2)  : http://localhost:9090/h2-console



