# Collection and processing of sensor data
The goal of this project was to build a centralized distributed system based on the model
client-server for sensing environmental parameters.
The sensor's task is to generate sensor readings, and to exchange, compare and calibrate its own readings with the readings of the geographically closest sensor.

The sensor consists of a web service client, gRPC client, gRPC server and has its own geo-location.
Sensors communicate with each other and exchange measurements via gRPC communication channel.
The server manages data about available registered sensors, theirs
geographical position and calibrated readings. All sensors communicate with server that offers web services.

## How the simulation works
a) We have one main server that provides web services to all of the running sensors -> this is implemented as a Spring Boot project
Navigate to the 'server' directory and run commands:
gradlew build
java -jar build\libs\server-0.0.1-SNAPSHOT.jar 

b) 'grpc-example' folder - implementation of a sensor object. Based of how many sensors you want to have in your simulation, 
    in multiple terminals run the program multiple times simultaneously, using the following commands:
  gradlew build
  java -cp build\libs\grpc-example-1.jar hr.fer.tel.rassus.sensorsReadings.SensorDevice
