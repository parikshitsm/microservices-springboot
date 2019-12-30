# microservices-springboot-v1
This project is used to create Microservices(java) using SpringBoot alongwith dependency with Lombok jar.
Below are some of the highlights of this project. 

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



