# wallet-service

Wallet-service is an application which exposes REST apis to manage credit and debit transactions of players. 

## Technical Stack

Java 11,
Spring-boot,
Maven,
JPA,
Spring HATEOS,
Spring data rest,
lombok,
h2 in memory db,
Swagger,
Jupiter with Mockito,
Spring boot test,
Docker,
Github,

## Steps to run the application

The application can be started in following different ways,

By running the application in local machine. Make sure the Java11 and a recent version of maven is installed in the system. Clone the repository using the previously mentioned clone command. Then build the application using  

mvn clean install

Then run the application using,
mvn spring-boot:run

By building docker image and running the docker. Make sure docker desktop is installed. Clone the repository and from the root, run the below command to build the docker image,

docker build -t wallet-service .

Then create and run docker container using the below command

docker run -ti -p 8080:8080 -v /Users/vipinv/Documents/tech_space/workspace/wallet-service/walletdb.mv.db:/walletdb.mv.db  wallet-service:latest bash

Make sure you replace the correct system path in the above command

By pulling the image from docker hub. The docker image of wallet-service application has been pushed to docker hub. Login to docker hub and pull the image using the below command,

docker pull vvinod2/wallet-service

Then run the image using the below,

docker run -ti -p 8080:8080 -v /Users/vipinv/Documents/tech_space/workspace/wallet-service/walletdb.mv.db:/walletdb.mv.db  vvinod2/wallet-service:latest bash

## Swagger Link

http://localhost:8080/walletservice/swagger-ui/#/

## Hal console link

http://localhost:8080/walletservice/browser/index.html#http://localhost:8080/walletservice/

